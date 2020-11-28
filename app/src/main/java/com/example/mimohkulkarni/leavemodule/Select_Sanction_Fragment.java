package com.example.mimohkulkarni.leavemodule;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Select_Sanction_Fragment extends Fragment {

    LocalDatabase localDatabase;
    LinearLayout topParentLayout;

    TextView TVnothing;

    Sanction_Fragment sanction_fragment = new Sanction_Fragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_sanction, container, false);

        SharedPreferences myPrefs = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLogin = myPrefs.getBoolean("RememberMe", false);
        if (!isLogin){
            Homepage_Activity.logout(getContext());
        }
        topParentLayout = view.findViewById(R.id.parentlayout);

        localDatabase = new LocalDatabase(getContext());
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

        TVnothing = view.findViewById(R.id.TVnothing);
        TVnothing.setVisibility(View.GONE);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

//        String show_leaves_url = "http://192.168.0.105/leavemodule/ShowLeaveList.php";
//        String show_leaves_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/ShowLeaveList.php";
        String show_leaves_url = "http://10.159.22.53/leavemodule/ShowLeaveList.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_leaves_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                processJsonDataForShow(response);
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

    private void processJsonDataForShow(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
            //Toast.makeText(getContext(),String.valueOf(number1),Toast.LENGTH_SHORT).show();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
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
                    TVoneone.setText(R.string.sanction_name);
                    TVoneone.setTextSize(20);
                    TVoneone.setGravity(Gravity.CENTER);
                    TVoneone.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVoneone.setBackgroundResource(R.drawable.shape);

                    final TextView TVonetwo = new TextView(getContext());
                    TVonetwo.setLayoutParams(TVparams);
                    TVonetwo.setText(R.string.sanction_pfno);
                    TVonetwo.setTextSize(20);
                    TVonetwo.setGravity(Gravity.CENTER);
                    TVonetwo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVonetwo.setBackgroundResource(R.drawable.shape);

                    final TextView TVonethree = new TextView(getContext());
                    TVonethree.setLayoutParams(TVparams);
                    TVonethree.setText(R.string.sanction_date);
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
                    TVtwoone.setText(jsonObject1.getString("name"));
                    TVtwoone.setTextSize(20);
                    TVtwoone.setGravity(Gravity.CENTER);
                    TVtwoone.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVtwoone.setBackgroundResource(R.drawable.shape);

                    final TextView TVtwotwo = new TextView(getContext());
                    TVtwotwo.setLayoutParams(TVparams);
                    TVtwotwo.setText(jsonObject1.getString("pfno"));
                    TVtwotwo.setTextSize(20);
                    TVtwotwo.setGravity(Gravity.CENTER);
                    TVtwotwo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    TVtwotwo.setBackgroundResource(R.drawable.shape);

                    final TextView TVtwothree = new TextView(getContext());
                    TVtwothree.setLayoutParams(TVparams);
                    TVtwothree.setText(jsonObject1.getString("date"));
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
                            String id = (String) v.getTag();
                            Bundle bundle = new Bundle();
                            bundle.putString("sid",id);
                            sanction_fragment.setArguments(bundle);
                            FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                            transaction.replace(R.id.fragment_container, sanction_fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
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
