package com.example.mimohkulkarni.leavemodule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class Leave_Application_Fragment extends Fragment implements View.OnClickListener {

    LocalDatabase localDatabase;
    Homepage_Fragment homepage_fragment = new Homepage_Fragment();
    Calendar mycalendar = Calendar.getInstance();

    Button reset,submit;
    TextView TVdisplay_diffdays,TVdisplay_name,TVdisplay_pfno,TVdisplay_desg,TVdisplay_station,TVdisplay_dept,TVdisplay_sec;
    EditText leavestartcalander,leaveendcalander,TVdisplay_leaveaddress,TVdisplay_leavepurpose,headstartcalander,headendcalander;
    ImageButton startreset,endreset,headstartreset,headendreset;
    Spinner spinnerleavenature,spinnerheadpermission,spinnerforwardauth;
    ArrayList<String> mainArrayList = new ArrayList<>();
    ArrayList<String> pfnoArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public String forwardpfno;
    Double diffdates;
    Boolean selectedfirst = true;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_application, container, false);

        leavestartcalander = view.findViewById(R.id.leavestartcalander);
        leaveendcalander = view.findViewById(R.id.leaveendcalander);
        headstartcalander = view.findViewById(R.id.headstartcalander);
        headendcalander = view.findViewById(R.id.headendcalander);
        TVdisplay_diffdays = view.findViewById(R.id.TVdisplay_diffdays);
        startreset = view.findViewById(R.id.startreset);
        endreset = view.findViewById(R.id.endreset);
        headstartreset = view.findViewById(R.id.headstartreset);
        headendreset = view.findViewById(R.id.headendreset);
        TVdisplay_name = view.findViewById(R.id.TVdisplay_name);
        TVdisplay_pfno = view.findViewById(R.id.TVdisplay_pfno);
        TVdisplay_desg = view.findViewById(R.id.TVdisplay_desg);
        TVdisplay_station = view.findViewById(R.id.TVdisplay_station);
        TVdisplay_dept = view.findViewById(R.id.TVdisplay_dept);
        TVdisplay_sec = view.findViewById(R.id.TVdisplay_sec);
        TVdisplay_leaveaddress = view.findViewById(R.id.TVdisplay_leaveaddress);
        TVdisplay_leavepurpose = view.findViewById(R.id.TVdisplay_leavepurpose);
        spinnerleavenature = view.findViewById(R.id.spinnerleavenature);
        spinnerheadpermission = view.findViewById(R.id.spinnerheadpermission);
        spinnerforwardauth = view.findViewById(R.id.spinnerforwardauth);
        headstartcalander.setEnabled(false);
        headendcalander.setEnabled(false);

        arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item,mainArrayList);
        spinnerforwardauth.setAdapter(arrayAdapter);
        spinnerforwardauth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String example = String.valueOf(position);
                if (!example.equals("0")){
                    forwardpfno = String.valueOf(pfnoArrayList.get(position));
                }
                else {
                    forwardpfno = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerheadpermission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemid = String.valueOf(id);
                if (itemid.equals("1")){
                    headstartcalander.setEnabled(true);
                    headendcalander.setEnabled(true);
                }
                else {
                    headstartcalander.setEnabled(false);
                    headendcalander.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        reset = view.findViewById(R.id.btnreset);
        submit = view.findViewById(R.id.btnsubmit);

        reset.setOnClickListener(this);
        submit.setOnClickListener(this);
        startreset.setOnClickListener(this);
        endreset.setOnClickListener(this);
        headstartreset.setOnClickListener(this);
        headendreset.setOnClickListener(this);

        localDatabase = new LocalDatabase(getContext());
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

        TVdisplay_name.setText(hashMap.get("name"));
        TVdisplay_pfno.setText(hashMap.get("pfno"));
        TVdisplay_desg.setText(hashMap.get("desg"));
        TVdisplay_station.setText(hashMap.get("station"));
        TVdisplay_dept.setText(hashMap.get("dept"));
        TVdisplay_sec.setText(hashMap.get("sec"));

//          String auth_input_url = "http://192.168.0.105/leavemodule/HigherAuth.php";
//        String auth_input_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/HigherAuth.php";
          String auth_input_url = "http://10.159.22.53/leavemodule/HigherAuth.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, auth_input_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                    mainArrayList.add("Select");
                    pfnoArrayList.add("nothing");
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

        date();
        
        return view;
    }

    private void date(){
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                leavestartcalander.setText(simpleDateFormat.format(mycalendar.getTime()));
                String cal = "Touch To Calculate";
                TVdisplay_diffdays.setText(cal);
                selectedfirst = true;
            }
        };

        leavestartcalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                leavestartcalander.setText(null);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener dateSetListener1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                leaveendcalander.setText(simpleDateFormat.format(mycalendar.getTime()));
                String cal = "Touch To Calculate";
                TVdisplay_diffdays.setText(cal);
                selectedfirst = true;
            }
        };

        leaveendcalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener1,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(mycalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                headstartcalander.setText(simpleDateFormat.format(mycalendar.getTime()));leaveendcalander.setText(simpleDateFormat.format(mycalendar.getTime()));
            }
        };

        headstartcalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DatePickerDialog datePickerDialog =new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener2,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
                datePickerDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener dateSetListener3 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mycalendar.set(Calendar.YEAR,year);
                mycalendar.set(Calendar.MONTH,month);
                mycalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                headendcalander.setText(simpleDateFormat.format(mycalendar.getTime()));
            }
        };

        headendcalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener3,mycalendar.get(Calendar.YEAR),
                        mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(mycalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        TVdisplay_diffdays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedfirst) {
                    if (!leavestartcalander.getText().toString().isEmpty() && !leaveendcalander.getText().toString().isEmpty()) {
                        try {
                            diffdates(leavestartcalander.getText().toString(), leaveendcalander.getText().toString());
                            selectedfirst = false;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please select both dates", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Please select dates again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void userChose(Double choice){
        String dayDiff1 = String.valueOf(choice);
        TVdisplay_diffdays.setText(dayDiff1);
    }

    public void diffdates(String date1,String date2) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd",Locale.US);

        Date startDay = simpleDateFormat.parse(date1);
        Date endDay = simpleDateFormat.parse(date2);
        long diff = (Math.abs(endDay.getTime() - startDay.getTime())) / (24 * 60 * 60 * 1000);
        diffdates = (double) diff;
        if (diffdates > 0) {
            Date date = Calendar.getInstance().getTime();
            String formattedDate = simpleDateFormat.format(date);
            if (date1.equals(formattedDate)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please Confirm");
                builder.setMessage("Does it include today's half day?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diffdates += 0.5;
                        userChose(diffdates);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userChose(diffdates);
                    }
                });
                builder.create().show();
            }
            else {
                userChose(diffdates);
            }
        }
        if (diffdates == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Please Confirm");
            builder.setMessage("Half Day or Full Day?");
            builder.setCancelable(false);
            builder.setPositiveButton("Half Day", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    diffdates = 0.5;
                    userChose(diffdates);
                }
            });
            builder.setNegativeButton("Full Day", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    diffdates = 1.0;
                    userChose(diffdates);
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startreset:
                leavestartcalander.setText(null);
                break;

            case R.id.endreset:
                leaveendcalander.setText(null);
                break;

            case R.id.btnsubmit:
                final String name = TVdisplay_name.getText().toString();
                final String pfno = TVdisplay_pfno.getText().toString();
                final String leavenature = spinnerleavenature.getSelectedItem().toString();
                final String leavefrom = leavestartcalander.getText().toString();
                final String leaveto = leaveendcalander.getText().toString();
                final String noofdays = TVdisplay_diffdays.getText().toString();
                final String leavepurpose = TVdisplay_leavepurpose.getText().toString();
                final String leaveaddress = TVdisplay_leaveaddress.getText().toString();
                final String headpermission = spinnerheadpermission.getSelectedItem().toString();
                final String headpermissionfrom = headstartcalander.getText().toString();
                final String headpermissionto = headendcalander.getText().toString();
                final String forwardto = forwardpfno;

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);

                if (!leavenature.equals("Select") && !leavefrom.isEmpty() && !leaveto.isEmpty() && !noofdays.equals("Touch to calculate")
                        && !leavepurpose.isEmpty() && !leaveaddress.isEmpty() && !forwardpfno.equals("nothing")){
                    if (headpermission.equals("Yes")){
                        if (headpermissionfrom.isEmpty() || headpermissionto.isEmpty()) break;
                    }
//                    String leave_submit_url = "http://192.168.0.105/leavemodule/LeaveApplication.php";
//                    String leave_submit_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/LeaveApplication.php";
                    String leave_submit_url = "http://10.159.22.53/leavemodule/LeaveApplication.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, leave_submit_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
//                           Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                            if (response.equals("Done")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Leave Submitted");
                                builder.setMessage("We will notify you after your leave is sanctioned.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_container, homepage_fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                                builder.create().show();
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
                            map.put("name",name);
                            map.put("pfno",pfno);
                            map.put("leavenature",leavenature);
                            map.put("leavefrom",leavefrom);
                            map.put("leaveto",leaveto);
                            map.put("noofdays",noofdays);
                            map.put("leavepurpose",leavepurpose);
                            map.put("leaveaddress",leaveaddress);
                            map.put("headpermission",headpermission);
                            map.put("headpermissionfrom",headpermissionfrom);
                            map.put("headpermissionto",headpermissionto);
                            map.put("forwardto",forwardto);
                            return map;
                        }
                    };
                    MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
                }
                else {
                    Toast.makeText(getContext(),"Please fill complete form.",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnreset:
                leavestartcalander.setText(null);
                leaveendcalander.setText(null);
                headstartcalander.setText(null);
                headendcalander.setText(null);
                spinnerleavenature.setSelection(0);
                String cal = "Touch To Calculate";
                TVdisplay_diffdays.setText(cal);
                TVdisplay_leavepurpose.setText(null);
                TVdisplay_leaveaddress.setText(null);
                spinnerheadpermission.setSelection(0);
                break;

            case R.id.headstartreset:
                headstartcalander.setText(null);
                break;

            case R.id.headendreset:
                headendcalander.setText(null);
                break;
        }

    }
}