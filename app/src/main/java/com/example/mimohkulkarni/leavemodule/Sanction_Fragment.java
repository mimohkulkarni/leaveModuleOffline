package com.example.mimohkulkarni.leavemodule;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Sanction_Fragment extends Fragment implements View.OnClickListener{

    LocalDatabase localDatabase;

    TextView TVdisplay_name,TVdisplay_pfno,TVdisplay_desg,TVdisplay_station,TVdisplay_dept,TVdisplay_leavenature,
            TVdisplay_leavestartcalander,TVdisplay_leaveendcalander,TVdisplay_noofdays,TVdisplay_leavepurpose,
            TVdisplay_leaveaddress,TVdisplay_headpermission,TVdisplay_headstartcalander,TVdisplay_headendcalander,TVdisplay_forwardedby,
            TVdisplay_mobile,TVdisplay_apdate;
    Spinner spinnerremark,spinnerforwardto;
    EditText remarkreason;
    Button confirm;
    String id,forwardpfno = "0",pfno,level,sanremark1,remarkreason1,ltype,lfrom,lto,nday;
    LinearLayout hideshowlayout,hideshowlayout1;
    ArrayList<String> mainArrayList = new ArrayList<>();
    ArrayList<String> pfnoArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sanction, container, false);

        if (getArguments() != null) {
            id = getArguments().getString("sid");
        }

        TVdisplay_name = view.findViewById(R.id.TVdisplay_name);
        TVdisplay_pfno = view.findViewById(R.id.TVdisplay_pfno);
        TVdisplay_desg = view.findViewById(R.id.TVdisplay_desg);
        TVdisplay_station = view.findViewById(R.id.TVdisplay_station);
        TVdisplay_dept = view.findViewById(R.id.TVdisplay_dept);
        TVdisplay_leavenature = view.findViewById(R.id.TVdisplay_leavenature);
        TVdisplay_leavestartcalander = view.findViewById(R.id.TVdisplay_leavestartcalander);
        TVdisplay_leaveendcalander = view.findViewById(R.id.TVdisplay_leaveendcalander);
        TVdisplay_noofdays = view.findViewById(R.id.TVdisplay_noofdays);
        TVdisplay_leavepurpose = view.findViewById(R.id.TVdisplay_leavepurpose);
        TVdisplay_leaveaddress = view.findViewById(R.id.TVdisplay_leaveaddress);
        TVdisplay_headpermission = view.findViewById(R.id.TVdisplay_headpermission);
        TVdisplay_headstartcalander = view.findViewById(R.id.TVdisplay_headstartcalander);
        TVdisplay_headendcalander = view.findViewById(R.id.TVdisplay_headendcalander);
        TVdisplay_forwardedby = view.findViewById(R.id.TVdisplay_forwardedby);
        TVdisplay_mobile = view.findViewById(R.id.TVdisplay_mobile);
        TVdisplay_apdate = view.findViewById(R.id.TVdisplay_apdate);
        spinnerremark = view.findViewById(R.id.spinnerremark);
        spinnerforwardto = view.findViewById(R.id.spinnerforwardto);
        remarkreason = view.findViewById(R.id.remarkreason);
        confirm = view.findViewById(R.id.btnconfirm);
        confirm.setOnClickListener(this);

        hideshowlayout = view.findViewById(R.id.hideshowlayout);
        hideshowlayout1 = view.findViewById(R.id.hideshowlayout1);
        hideshowlayout.setVisibility(View.GONE);
        hideshowlayout1.setVisibility(View.GONE);

        localDatabase = new LocalDatabase(getContext());
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

        pfno = hashMap.get("pfno");
        level = hashMap.get("level");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

//        String sanction_url = "http://192.168.0.105/leavemodule/ShowLeave.php";
//        String sanction_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/ShowLeave.php";
        String sanction_url = "http://10.159.22.53/leavemodule/ShowLeave.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sanction_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getString("hqperm").equals("No")){
                            String na = "N.A.";
                            TVdisplay_headstartcalander.setText(na);
                            TVdisplay_headendcalander.setText(na);
                        }
                        else {
                            TVdisplay_headstartcalander.setText(jsonObject1.getString("hqperfrm"));
                            TVdisplay_headendcalander.setText(jsonObject1.getString("hqperto"));
                        }
                        TVdisplay_name.setText(jsonObject1.getString("name"));
                        TVdisplay_pfno.setText(jsonObject1.getString("pfno"));
                        TVdisplay_desg.setText(jsonObject1.getString("desig"));
                        TVdisplay_station.setText(jsonObject1.getString("station"));
                        TVdisplay_dept.setText(jsonObject1.getString("dept"));
                        TVdisplay_leavenature.setText(jsonObject1.getString("ltype"));
                        TVdisplay_leavestartcalander.setText(jsonObject1.getString("lfrom"));
                        TVdisplay_leaveendcalander.setText(jsonObject1.getString("lto"));
                        TVdisplay_noofdays.setText(jsonObject1.getString("nday"));
                        TVdisplay_leavepurpose.setText(jsonObject1.getString("purpose"));
                        TVdisplay_leaveaddress.setText(jsonObject1.getString("address"));
                        TVdisplay_headpermission.setText(jsonObject1.getString("hqperm"));
                        TVdisplay_forwardedby.setText(jsonObject1.getString("fname"));
                        TVdisplay_mobile.setText(jsonObject1.getString("mob"));
                        TVdisplay_apdate.setText(jsonObject1.getString("apdate"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id",id);
                return map;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        spinnerremark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0 || id == 1){
                    hideshowlayout.setVisibility(View.GONE);
                    hideshowlayout1.setVisibility(View.GONE);
                }
                if (id == 2){
                    hideshowlayout.setVisibility(View.VISIBLE);
                    hideshowlayout1.setVisibility(View.GONE);
                }
                if (id == 3){
                    hideshowlayout.setVisibility(View.GONE);
                    hideshowlayout1.setVisibility(View.VISIBLE);


//                    String auth_input_url = "http://192.168.0.105/leavemodule/HigherAuth.php";
//                    String auth_input_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/HigherAuth.php";
                    String auth_input_url = "http://10.159.22.53/leavemodule/HigherAuth.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, auth_input_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                                mainArrayList.add("Select");
                                pfnoArrayList.add("0");
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    pfnoArrayList.add(jsonObject1.getString("pfno"));
                                    mainArrayList.add(jsonObject1.getString("name"));
                                }
                                arrayAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("level",hashMap.get("level"));
                            return map;
                        }
                    };
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item,mainArrayList);
        spinnerforwardto.setAdapter(arrayAdapter);
        spinnerforwardto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String example = String.valueOf(position);
                if (!example.equals("0")){
                    forwardpfno = String.valueOf(pfnoArrayList.get(position));
                }
                else {
                    forwardpfno = "0";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnconfirm:
                sanremark1 = spinnerremark.getSelectedItem().toString();
                String spinnerforwardto1;
                if (sanremark1.equals("Forward")){
                    spinnerforwardto1 = spinnerforwardto.getSelectedItem().toString();
                }
                else {
                    spinnerforwardto1 = " ";
                }
                remarkreason1 = remarkreason.getText().toString();
                if (remarkreason1.isEmpty()) remarkreason1 = " ";
                if (forwardpfno.equals("0")) forwardpfno = " ";
                ltype = TVdisplay_leavenature.getText().toString();
                lfrom = TVdisplay_leavestartcalander.getText().toString();
                lto = TVdisplay_leaveendcalander.getText().toString();
                nday = TVdisplay_noofdays.getText().toString();
                if (!sanremark1.equals("Select")) {
                    if (sanremark1.equals("Sanctioned") || (sanremark1.equals("Not Sanctioned") && !remarkreason.getText().toString().isEmpty())
                            || (sanremark1.equals("Forward") && !spinnerforwardto1.equals("Select"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Please Confirm");
                        builder.setMessage("Are you sure to perform this action?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Please Wait...");
                                progressDialog.show();
                                progressDialog.setCancelable(false);

//                                String leave_submit_url = "http://192.168.0.105/leavemodule/LeaveSanctionSubmit.php";
//                                String leave_submit_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/LeaveSanctionSubmit.php";
                                String leave_submit_url = "http://10.159.22.53/leavemodule/LeaveSanctionSubmit.php";

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, leave_submit_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
//                                        Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        HashMap<String,String> hashMap = localDatabase.getuserdata();
                                        String startNumber = hashMap.get("pending");
//                                        Toast.makeText(getContext(),startNumber,Toast.LENGTH_SHORT).show();
                                        int finalNumber = Integer.valueOf(startNumber) - 1;
                                        if (response.equals("Done ")){
                                            Select_Sanction_Fragment select_sanction_fragment = new Select_Sanction_Fragment();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, select_sanction_fragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                            Toast.makeText(getContext(),"Action taken successfully.",Toast.LENGTH_LONG).show();
                                            localDatabase.updatenumber(String.valueOf(finalNumber));
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Error,something happened.",Toast.LENGTH_LONG).show();
                                            Select_Sanction_Fragment select_sanction_fragment = new Select_Sanction_Fragment();
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, select_sanction_fragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> map = new HashMap<>();
                                        map.put("sid",id);
                                        map.put("sanremark",sanremark1);
                                        map.put("sanreason",remarkreason1);
                                        map.put("forwardto1",forwardpfno);
                                        map.put("ltype",ltype);
                                        map.put("lfrom",lfrom);
                                        map.put("lto",lto);
                                        map.put("nday",nday);
                                        map.put("pfno",pfno);
                                        map.put("level",level);
                                        return map;
                                    }
                                };
                                MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                    else {
                        Toast.makeText(getContext(),"Please dont keep fields empty",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"Select action before giving remark",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}