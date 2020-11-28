package com.example.mimohkulkarni.leavemodule;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class Leave_Status_Fragment extends Fragment {
    LocalDatabase localDatabase;

    TextView TVnothing,TVdisplay_leavetype,TVdisplay_leavefrom,TVdisplay_leaveto,TVdisplay_forward,TVdisplay_status;
    View view,view1;
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_leave_status, container, false);

        SharedPreferences myPrefs = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLogin = myPrefs.getBoolean("RememberMe", false);
        if (!isLogin){
            Homepage_Activity.logout(getContext());
        }

        localDatabase = new LocalDatabase(getContext());
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

        TVnothing = view.findViewById(R.id.TVnothing);
        TVnothing.setVisibility(View.GONE);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

//        String show_leaves_url = "http://192.168.0.105/leavemodule/LeaveStatus.php";
//        String show_leaves_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/LeaveStatus.php";
        String show_leaves_url = "http://10.159.22.53/leavemodule/LeaveStatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_leaves_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                processJsonDataForLeaves(response);
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
                map.put("pfno",hashMap.get("pfno"));
                return map;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        return view;
    }

    private void processJsonDataForLeaves(String response) {
        LinearLayout topParentLayout = view.findViewById(R.id.parentlayout);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
//                Toast.makeText(getContext(),json,Toast.LENGTH_SHORT).show();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    final String sid = jsonObject1.getString("id");

                    LinearLayout childOneParent = new LinearLayout(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 10;
                    layoutParams.rightMargin = 10;
                    childOneParent.setLayoutParams(layoutParams);
                    childOneParent.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout childOneOneParent = new LinearLayout(getContext());
                    childOneOneParent.setWeightSum(3);
                    childOneOneParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    childOneOneParent.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayout.LayoutParams TVparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    TVparams.weight = 1;
                    TVparams.width = 0;

                    final TextView TVoneone = new TextView(getContext());
                    TVoneone.setLayoutParams(TVparams);
                    TVoneone.setText(R.string.status_leave_type);
                    TVoneone.setTextSize(20);
                    TVoneone.setGravity(Gravity.CENTER);
                    TVoneone.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVoneone.setBackgroundResource(R.drawable.shape);

                    final TextView TVonetwo = new TextView(getContext());
                    TVonetwo.setLayoutParams(TVparams);
                    TVonetwo.setText(R.string.sanction_date);
                    TVonetwo.setTextSize(20);
                    TVonetwo.setGravity(Gravity.CENTER);
                    TVonetwo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVonetwo.setBackgroundResource(R.drawable.shape);

                    final TextView TVonethree = new TextView(getContext());
                    TVonethree.setLayoutParams(TVparams);
                    TVonethree.setText(R.string.status_remark);
                    TVonethree.setTextSize(20);
                    TVonethree.setGravity(Gravity.CENTER);
                    TVonethree.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVonethree.setBackgroundResource(R.drawable.shape);

                    LinearLayout childOneTwoParent = new LinearLayout(getContext());
                    childOneTwoParent.setWeightSum(3);
                    childOneTwoParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    childOneTwoParent.setOrientation(LinearLayout.HORIZONTAL);

                    final TextView TVtwoone = new TextView(getContext());
                    TVtwoone.setLayoutParams(TVparams);
                    TVtwoone.setText(jsonObject1.getString("ltype"));
                    TVtwoone.setTextSize(20);
                    TVtwoone.setGravity(Gravity.CENTER);
                    TVtwoone.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVtwoone.setBackgroundResource(R.drawable.shape);

                    final TextView TVtwotwo = new TextView(getContext());
                    TVtwotwo.setLayoutParams(TVparams);
                    TVtwotwo.setText(jsonObject1.getString("date"));
                    TVtwotwo.setTextSize(20);
                    TVtwotwo.setGravity(Gravity.CENTER);
                    TVtwotwo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVtwotwo.setBackgroundResource(R.drawable.shape);

                    final TextView TVtwothree = new TextView(getContext());
                    TVtwothree.setLayoutParams(TVparams);
                    String sanremark = jsonObject1.getString("sanremark");
                    if (sanremark.equals("")) sanremark = "Pending";
                    if (sanremark.equals("Forward")) sanremark = "Forwarded";
                    TVtwothree.setText(sanremark);
                    TVtwothree.setTextSize(20);
                    TVtwothree.setGravity(Gravity.CENTER);
                    TVtwothree.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVtwothree.setBackgroundResource(R.drawable.shape);

                    Space space = new Space(getContext());
                    LinearLayout.LayoutParams Spaceparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    Spaceparams.height = 150;
                    space.setLayoutParams(Spaceparams);

                    topParentLayout.addView(childOneParent);
                    childOneParent.addView(childOneOneParent);
                    childOneParent.addView(childOneTwoParent);

                    childOneOneParent.addView(TVoneone);
                    childOneOneParent.addView(TVonetwo);
                    childOneOneParent.addView(TVonethree);

                    childOneTwoParent.addView(TVtwoone);
                    childOneTwoParent.addView(TVtwotwo);
                    childOneTwoParent.addView(TVtwothree);

                    childOneParent.addView(space);
                    childOneParent.setTag(sid);

                    childOneParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String id = (String) v.getTag();
//                            String sanction_url = "http://192.168.0.102/leavemodule/ShowLeave.php";
//                            String sanction_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/ShowLeave.php";
                            String sanction_url = "http://10.159.22.53/leavemodule/ShowLeave.php";

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, sanction_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                    Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                                    try {
                                        JSONObject jsonObject10 = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject10.getJSONArray("resultarray");
                                        final JSONObject jsonObject11 = jsonArray.getJSONObject(0);

                                        layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.YOUR_STYLE);
                                        if (layoutInflater != null) {
                                            view1 = layoutInflater.inflate(R.layout.activity_dialog,null);
                                        }

                                        TVdisplay_leavetype = view1.findViewById(R.id.TVdisplay_leavetype);
                                        TVdisplay_leavefrom = view1.findViewById(R.id.TVdisplay_leavefrom);
                                        TVdisplay_leaveto = view1.findViewById(R.id.TVdisplay_leaveto);
                                        TVdisplay_forward = view1.findViewById(R.id.TVdisplay_forward);
                                        TVdisplay_status = view1.findViewById(R.id.TVdisplay_status);

                                        Dialog dialog = new Dialog(contextThemeWrapper);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(view1);
                                        TVdisplay_leavetype.setText(jsonObject11.getString("ltype"));
                                        TVdisplay_leavefrom.setText(jsonObject11.getString("lfrom"));
                                        TVdisplay_leaveto.setText(jsonObject11.getString("lto"));
//                                        Toast.makeText(getContext(),jsonObject11.getString("sanremark"),Toast.LENGTH_LONG).show();
                                        TVdisplay_forward.setText(jsonObject11.getString("forword"));
                                        String sanremark = jsonObject11.getString("sanremark");
                                        if (sanremark.equals("")) sanremark = "Pending";
                                        if (sanremark.equals("Forward")) sanremark = "Forwarded";
                                        TVdisplay_status.setText(sanremark);
                                        dialog.show();
                                    } catch (JSONException e) {
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
//                            Toast.makeText(getContext(),id,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else {
                TVnothing.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
