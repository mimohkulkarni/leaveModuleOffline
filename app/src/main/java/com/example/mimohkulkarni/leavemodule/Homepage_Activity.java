package com.example.mimohkulkarni.leavemodule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Homepage_Activity extends AppCompatActivity implements View.OnClickListener{

    LocalDatabase localDatabase = new LocalDatabase(Homepage_Activity.this);
//    private InterstitialAd mInterstitialAd;
    Boolean isNotification = false;

    Homepage_Fragment homepage_fragment = new Homepage_Fragment();
    Leave_Fragment leave_fragment = new Leave_Fragment();
    Fragment personal_info_fragment = new Personal_Info_Fragment();
    Select_Sanction_Fragment select_sanction_fragment = new Select_Sanction_Fragment();

    Button home,leave,personal_info,logout;
    TextView TVwelcome,TVname;
    int doubleBackToExitPressed = 1;
//    private static final int INTERVAL = 1000 ;
    String active_color = "#C48E31";
    String passive_color = "#E6A73A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5547365499588155/8518506393");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener(){
//            public void onAdLoaded(){
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//        });

        Bundle extraBundle = getIntent().getExtras();
        if(extraBundle != null){
            isNotification = extraBundle.getBoolean("isNotification",false);
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        home = findViewById(R.id.homepage);
        leave = findViewById(R.id.leave);
        personal_info = findViewById(R.id.personal_info);
        logout = findViewById(R.id.logout);
        leave.setOnClickListener(this);
        personal_info.setOnClickListener(this);
        logout.setOnClickListener(this);
        home.setOnClickListener(this);
        TVwelcome = findViewById(R.id.title);

        HashMap<String,String> hashMap = localDatabase.getuserdata();
        String welcome = "Welcome " + hashMap.get("name");
        TVwelcome.setText(welcome);

        Calendar calendar = Calendar.getInstance();
        Intent intent_Pending = new Intent(this, BGPendingBroadcast.class);
        final int noti_id1 = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), noti_id1, intent_Pending, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HOUR*3,pendingIntent);
        }

        if (!isNotification) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, homepage_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, select_sanction_fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(this,Settings_Activity.class));
        }
        if (item.getItemId() == R.id.password){
            startActivity(new Intent(this,Password_Change_Activity.class));
        }
//        if (item.getItemId() == android.R.id.home){
//            if (getFragmentManager().getBackStackEntryCount() > 0) {
//                getFragmentManager().popBackStack();
//            } else {
//                super.onBackPressed();
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (homepage_fragment.isVisible()) ishomepage();
        if (personal_info_fragment.isVisible() || leave_fragment.isVisible()){
            home.setBackgroundColor(Color.parseColor(active_color));
            leave.setBackgroundColor(Color.parseColor(passive_color));
            personal_info.setBackgroundColor(Color.parseColor(passive_color));
            addFragment(homepage_fragment);
        }
        else {
            backpressed();
        }
    }

    public void backpressed(){
        if (!homepage_fragment.isVisible() && !personal_info_fragment.isVisible() && !leave_fragment.isVisible()) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    public static void logout(Context context){
        LocalDatabase localDatabase = new LocalDatabase(context.getApplicationContext());
        localDatabase.deleteuser();
        Toast.makeText(context.getApplicationContext(),"Logout Successful",Toast.LENGTH_SHORT).show();
        SharedPreferences myPrefs = context.getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean("RememberMe", false);
        prefsEditor.putBoolean("isLoggedIn",false);
        prefsEditor.putBoolean("opennoti",false);
        prefsEditor.apply();
        context.startActivity(new Intent(context,Login_Activity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave:
                addFragment(leave_fragment);
                personal_info.setBackgroundColor(Color.parseColor(passive_color));
                home.setBackgroundColor(Color.parseColor(passive_color));
                leave.setBackgroundColor(Color.parseColor(active_color));
                break;

            case R.id.homepage:
                addFragment(homepage_fragment);
                personal_info.setBackgroundColor(Color.parseColor(passive_color));
                home.setBackgroundColor(Color.parseColor(active_color));
                leave.setBackgroundColor(Color.parseColor(passive_color));
                break;

            case R.id.logout:
                logout(this);
                break;

            case R.id.personal_info:
                addFragment(personal_info_fragment);
                personal_info.setBackgroundColor(Color.parseColor(active_color));
                home.setBackgroundColor(Color.parseColor(passive_color));
                leave.setBackgroundColor(Color.parseColor(passive_color));
                break;
        }
    }

    public void addFragment(Fragment someFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void ishomepage(){
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isRemember = myPrefs.getBoolean("RememberMe", false);
        if (!isRemember) {
            if (doubleBackToExitPressed == 2) {
                logout(this);
            }
            else {
                doubleBackToExitPressed ++;
                Toast.makeText(this, "Pressing back again will logout", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed = 1;
                }
            }, 2000);
        }
        else {
            if (doubleBackToExitPressed == 2) {
                finish();
                finishAffinity();
                System.exit(0);
            } else {
                doubleBackToExitPressed++;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressed = 1;
                }
            }, 2000);
        }
    }
}