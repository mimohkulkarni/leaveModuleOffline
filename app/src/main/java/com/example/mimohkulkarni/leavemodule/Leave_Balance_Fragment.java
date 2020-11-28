package com.example.mimohkulkarni.leavemodule;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class Leave_Balance_Fragment extends Fragment {

    LocalDatabase localDatabase;
//    public AdView mAdView;

    TextView TVdisplay_clleave,TVdisplay_aplleave,TVdisplay_cclleave,TVdisplay_haplleave,TVdisplay_rhleave;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_balance, container, false);

        localDatabase = new LocalDatabase(getContext());
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

//        mAdView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        TVdisplay_clleave = view.findViewById(R.id.TVdisplay_clleave);
        TVdisplay_aplleave = view.findViewById(R.id.TVdisplay_aplleave);
        TVdisplay_cclleave = view.findViewById(R.id.TVdisplay_cclleave);
        TVdisplay_haplleave = view.findViewById(R.id.TVdisplay_haplleave);
        TVdisplay_rhleave = view.findViewById(R.id.TVdisplay_rhleave);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

//        String balance_url = "http://192.168.0.105/leavemodule/LeaveBalance.php";
//        String balance_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/LeaveBalance.php";
        String balance_url = "http://10.159.22.53/leavemodule/LeaveBalance.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, balance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                int a = Integer.valueOf(processData(response,0));
                int b = Integer.valueOf(processData(response,1));
                int c = Integer.valueOf(processData(response,2));
                int d = Integer.valueOf(processData(response,3));
                int e = Integer.valueOf(processData(response,4));
                String colora = setcolor(a);
                String colorb = setcolor(b);
                String colorc = setcolor(c);
                String colord = setcolor(d);
                String colore = setcolor(e);

                TVdisplay_clleave.setTextColor(Color.parseColor(colora));
                TVdisplay_rhleave.setTextColor(Color.parseColor(colorb));
                TVdisplay_aplleave.setTextColor(Color.parseColor(colorc));
                TVdisplay_haplleave.setTextColor(Color.parseColor(colord));
                TVdisplay_cclleave.setTextColor(Color.parseColor(colore));

                String clleave = a + " Days";
                String rhleave =  b + " Days";
                String aplleave = c + " Days";
                String haplleave =  d + " Days";
                String cclleave =  e + " Days";
                TVdisplay_clleave.setText(clleave);
                TVdisplay_rhleave.setText(rhleave);
                TVdisplay_aplleave.setText(aplleave);
                TVdisplay_haplleave.setText(haplleave);
                TVdisplay_cclleave.setText(cclleave);
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

    private String setcolor(int value){
        String colour1 = "#00BF00";
        String colour2 = "#D9AD00";
        String colour3 = "#F20000";
        String defaultcolor = "#000000";
        if (value > 275)  return colour1;
        if (value < 275 && value > 100) return colour2;
        if (value < 100) return  colour3;
        else return defaultcolor;
    }

    private String processData(String data, int returanable){
        String[] part = data.split("#");
        String clleave = part[0];
        String rhleave = part[1];
        String aplleave = part[2];
        String haplleave = part[3];
        String cclleave = part[4];

        switch (returanable){
            case 0:
                return clleave;

            case 1:
                return rhleave;

            case 2:
                return aplleave;

            case 3:
                return haplleave;

            case 4:
                return cclleave;

            default:
                return null;
        }
    }
}
