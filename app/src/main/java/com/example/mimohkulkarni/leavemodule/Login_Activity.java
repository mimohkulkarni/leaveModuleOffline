package com.example.mimohkulkarni.leavemodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity{

    LocalDatabase localDatabase;
//    public AdView mAdView;
//    private InterstitialAd mInterstitialAd;
        ProgressDialog progressDialog;

    EditText ETusername,ETpassword;
    Button login;
    ImageButton forgotpass;
//    int doubleBackToExitPressed = 1;
    private String username,password;
    private static final String NO_CONNECTION_MESSAGE = "Looks like there is no connection. Please check you are connected to the network and restart the app.";
    private static final String NO_CONNECTION_TITLE = "No Connection";
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//        MobileAds.initialize(this, "ca-app-pub-5547365499588155~5838624310");
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5547365499588155/3873873748" +
//                "");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//        });

        rememberMe = findViewById(R.id.rememberMe);
        ETusername = findViewById(R.id.username);
        ETpassword = findViewById(R.id.password);
        forgotpass = findViewById(R.id.forgotpass);
        rememberMe.setChecked(true);
        login = findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        localDatabase = new LocalDatabase(this);
        if (isNetworkConnected()){
            SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
            boolean isRemember = myPrefs.getBoolean("RememberMe", false);
            if (isRemember){
                username = myPrefs.getString("LOGIN_ID", "");
                password = myPrefs.getString("LOGIN_PSWD", "");
                ETusername.setText(username);
                ETpassword.setText(password);
                onLogin();
            }

            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forgotpass_intent = new Intent(Login_Activity.this,Password_Change_Activity.class);
                    forgotpass_intent.putExtra("isLoginActivity",true);
                    startActivity(forgotpass_intent);
                }
            });

        }
        else {
            alertbox(NO_CONNECTION_TITLE,NO_CONNECTION_MESSAGE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        finishAffinity();
        System.exit(0);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null;
    }

    public void onLogin(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertbox(NO_CONNECTION_TITLE,NO_CONNECTION_MESSAGE);
            }
        }, 11000);

        username = ETusername.getText().toString();
        password = ETpassword.getText().toString();
        progressDialog = new ProgressDialog(Login_Activity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

//          String login_url = "http://192.168.0.105/leavemodule/Login.php";
//        String login_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/Login.php";
          String login_url = "http://10.159.22.53/leavemodule/Login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                processJsonData(response);
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
                map.put("um",username);
                map.put("pw",password);
                return map;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void alertbox(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                finishAffinity();
                System.exit(0);
            }
        });
        builder.create();
        builder.show();
    }
    private void processJsonData(String result){
        if (!result.equals("Error")) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");

                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String pfno = jsonObject1.getString("pfno");
                String name = jsonObject1.getString("name");
                String panno = jsonObject1.getString("panno");
                String mobile = jsonObject1.getString("mobile");
                String desg = jsonObject1.getString("desig");
                String bdate = jsonObject1.getString("bdate");
                String station = jsonObject1.getString("station");
                String dept = jsonObject1.getString("dept");
                String level = jsonObject1.getString("level");
                String apdate = jsonObject1.getString("apdate");
                String dsec = jsonObject1.getString("dsec");
                String viipay = jsonObject1.getString("viipay");
                String viilvl = jsonObject1.getString("viilvl");
                String sec = jsonObject1.getString("sec");
                String catg = jsonObject1.getString("catg");
                String nxtinc = jsonObject1.getString("nxtinc");

                if (panno.isEmpty()) panno = "N.A.";
                if (mobile.isEmpty()) mobile = "N.A.";
                if (desg.isEmpty()) desg = "N.A.";
                if (bdate.equals("0000-00-00")) bdate = "N.A.";
                if (station.isEmpty()) station = "N.A.";
                if (dept.isEmpty()) dept = "N.A.";
                if (level.isEmpty()) level = "0";
                if (apdate.equals("0000-00-00")) apdate = "N.A.";
                if (dsec.equals("0000-00-00")) dsec = "N.A.";
                if (viipay.equals("0")) viipay = "N.A.";
                if (viilvl.equals("0")) viilvl = "N.A.";
                if (sec.isEmpty()) sec = "N.A.";
                if (catg.isEmpty()) catg = "N.A.";
                if (nxtinc.equals(" - 0") || nxtinc.equals("0 - ") || nxtinc.equals("0 - 0")
                        || nxtinc.equals("  -  ")) nxtinc = "N.A.";

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("pfno", pfno);
                hashMap.put("name", name);
                hashMap.put("panno", panno);
                hashMap.put("mobile", mobile);
                hashMap.put("desg", desg);
                hashMap.put("bdate", bdate);
                hashMap.put("station", station);
                hashMap.put("dept", dept);
                hashMap.put("level", level);
                hashMap.put("apdate", apdate);
                hashMap.put("dsec", dsec);
                hashMap.put("viipay", viipay);
                hashMap.put("viilvl",viilvl);
                hashMap.put("sec", sec);
                hashMap.put("catg", catg);
                hashMap.put("nextinc", nxtinc);

                LocalDatabase localDatabase = new LocalDatabase(Login_Activity.this);
                localDatabase.deleteuser();
                Boolean isInserted = localDatabase.adduser(hashMap);
                if (isInserted) {
                    SharedPreferences myPrefs = Login_Activity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = myPrefs.edit();
                    if (rememberMe.isChecked()) {
                        prefsEditor.putString("LOGIN_ID", username);
                        prefsEditor.putString("LOGIN_PSWD", password);
                        prefsEditor.putBoolean("RememberMe", true);
                        prefsEditor.putBoolean("isLoggedIn",true);
                        prefsEditor.putBoolean("notification",true);
                        prefsEditor.apply();
                    }
                    else{
                        prefsEditor.putBoolean("RememberMe",false);
                        prefsEditor.putBoolean("isLoggedIn",true);
                        prefsEditor.putBoolean("notification",true);
                        prefsEditor.apply();
                    }
                    Toast.makeText(Login_Activity.this,"Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login_Activity.this, Homepage_Activity.class));
                }
                else {
                    Toast.makeText(Login_Activity.this, "Error,please try again.If error presists reinstall the app.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(Login_Activity.this, "Incorrect Combination of Username and Password", Toast.LENGTH_LONG).show();
        }
    }
}