package com.example.mimohkulkarni.leavemodule;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Password_Change_Activity extends AppCompatActivity{

    LocalDatabase localDatabase;
//    public AdView mAdView;

    EditText EToldpass,ETnewpassfist,ETnewpasssecond,editText,ETpfno;
    ImageButton forgotpass;
    Button btnsubmit,btndialog;
    Boolean isLoginActivity = false;
    private String OTP_CHECK;
    String textotp,otpmessage,txtmessage,name,mobile;
    LinearLayout linearLayout,linearLayouttohide,linearLayouttohide1;
    Space spacehide1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        Bundle extraBundle = getIntent().getExtras();
        if(extraBundle != null){
            isLoginActivity = extraBundle.getBoolean("isLoginActivity");
        }

        checkAndRequestPermissions();


        localDatabase = new LocalDatabase(this);
        final HashMap<String,String> hashMap = localDatabase.getuserdata();

        EToldpass = findViewById(R.id.EToldpass);
        ETnewpassfist = findViewById(R.id.ETnewpassfirst);
        ETnewpasssecond = findViewById(R.id.ETnewpasssecond);
        forgotpass = findViewById(R.id.forgotpass);
        btnsubmit = findViewById(R.id.btnsubmit);
        ETpfno = findViewById(R.id.ETpfno);
        linearLayouttohide = findViewById(R.id.linearlayouttohide);
        linearLayouttohide1 = findViewById(R.id.linearlayouttohide1);
        spacehide1 = findViewById(R.id.spacehide1);

        if (!isLoginActivity){
            linearLayouttohide1.setVisibility(View.GONE);
            spacehide1.setVisibility(View.GONE);
            ETnewpassfist.setEnabled(false);
            ETnewpasssecond.setEnabled(false);
        }
        else {
            linearLayouttohide.setVisibility(View.GONE);
        }

        final BGSendMessage bgSendMessage = new BGSendMessage();

        EToldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ETnewpassfist.setEnabled(true);
                    ETnewpasssecond.setEnabled(true);
                }
                else {
                    ETnewpassfist.setEnabled(false);
                    ETnewpasssecond.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        r.nextInt((max-min)+1)+min;((900-601)+1)+601
        final int otpPart1 = new Random().nextInt(200) + 101; //101-300
        final int otpPart2 = new Random().nextInt(300) + 601; //900-601
        OTP_CHECK = String.valueOf(otpPart1) + String.valueOf(otpPart2);
//        Toast.makeText(this,String.valueOf(otpPart1) + "#" + String.valueOf(otpPart2),Toast.LENGTH_SHORT).show();
//        OTP_CHECK = 121212;

        otpmessage = "Your One Time Password for resetting password is " + OTP_CHECK + ".Do not disclose it to others.";


        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        layoutParams.gravity = Gravity.CENTER;
        linearLayout.removeView(editText);
        linearLayout.setGravity(Gravity.CENTER);

        final LinearLayout.LayoutParams TVparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        TVparams.gravity = Gravity.CENTER;

        editText = new EditText(this);
        editText.setLayoutParams(TVparams);
        editText.setHint("______");
        editText.setSingleLine();
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editText.setLetterSpacing(0.5f);
        editText.setGravity(Gravity.CENTER);

        linearLayout.addView(editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textotp = editText.getText().toString();
                if (textotp.length() == 6){
                    editText.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgSendMessage.execute(hashMap.get("mobile"),otpmessage,hashMap.get("pfno"),"OTP","","");
                final AlertDialog.Builder builder = new AlertDialog.Builder(Password_Change_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("Enter OTP");
                builder.setView(linearLayout);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!editText.getText().toString().isEmpty()){
                            if (editText.getText().toString().equals(OTP_CHECK)){
                                Toast.makeText(Password_Change_Activity.this,"Done",Toast.LENGTH_SHORT).show();
                                EToldpass.setText("");
                                EToldpass.setEnabled(false);
                                forgotpass.setEnabled(false);
                                ETnewpassfist.setEnabled(true);
                                ETnewpasssecond.setEnabled(true);
                            }
                            else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Password_Change_Activity.this);
                                builder1.setCancelable(false);
                                builder1.setTitle("ALERT");
                                builder1.setMessage("Wrong OTP");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        if (!isLoginActivity) startActivity(new Intent(Password_Change_Activity.this,Homepage_Activity.class));
                                        else startActivity(new Intent(Password_Change_Activity.this,Login_Activity.class));
                                    }
                                });
                                builder1.create().show();
                                ETnewpassfist.setEnabled(false);
                                ETnewpasssecond.setEnabled(false);
                            }
                        }
                        else {
                            Toast.makeText(Password_Change_Activity.this,"Please Enter OTP",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getIntent()));
                        }
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isLoginActivity) startActivity(new Intent(Password_Change_Activity.this,Homepage_Activity.class));
                        else startActivity(new Intent(Password_Change_Activity.this,Login_Activity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                editText.requestFocus();
                btndialog = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

            }
        });

        ETpfno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String color = "#000000";
                    ETpfno.setTextColor(Color.parseColor(color));
                    ETnewpassfist.setEnabled(true);
                    ETnewpasssecond.setEnabled(true);
                }
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLoginActivity) {
                    txtmessage = hashMap.get("name") + " your password has been succesfully changed.If you didn't do this contact you admin.";
                    if (EToldpass.isEnabled()) {
                        String oldpass;
                        oldpass = EToldpass.getText().toString();
                        try {
                            String result = bgSendMessage.execute(hashMap.get("mobile"), txtmessage, hashMap.get("pfno"), "CHECK", "", oldpass).get();
//                            Toast.makeText(Password_Change_Activity.this,result,Toast.LENGTH_SHORT).show();
                            if (result.equals("Done")) {
                                changepass(hashMap.get("mobile"),hashMap.get("pfno"));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Password_Change_Activity.this);
                                builder.setTitle("Error");
                                builder.setMessage("Your old password does not match with exising.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(new Intent(getIntent()));
                                    }
                                });
                                builder.create().show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        changepass(hashMap.get("mobile"),hashMap.get("pfno"));
                    }
                }
                else {
                    final String pfno = ETpfno.getText().toString();
                    String newpassfirst = ETnewpassfist.getText().toString();
                    String newpasssecond = ETnewpasssecond.getText().toString();
                    if (checkPFNo(pfno)){
                        if (newpassfirst.equals(newpasssecond) && !newpassfirst.isEmpty()) {
                            bgSendMessage.execute(mobile,otpmessage,pfno,"OTP","","");
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Password_Change_Activity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Enter OTP");
                            builder.setView(linearLayout);
                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!editText.getText().toString().isEmpty()){
                                        if (editText.getText().toString().equals(OTP_CHECK)){
                                            Toast.makeText(Password_Change_Activity.this,"Done",Toast.LENGTH_SHORT).show();
                                            EToldpass.setText("");
                                            EToldpass.setEnabled(false);
                                            forgotpass.setEnabled(false);
                                            ETnewpassfist.setEnabled(true);
                                            ETnewpasssecond.setEnabled(true);
                                            changepass(mobile,pfno);
                                        }
                                        else {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Password_Change_Activity.this);
                                            builder1.setCancelable(false);
                                            builder1.setTitle("ALERT");
                                            builder1.setMessage("Wrong OTP");
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                    if (!isLoginActivity) startActivity(new Intent(Password_Change_Activity.this,Homepage_Activity.class));
                                                    else startActivity(new Intent(Password_Change_Activity.this,Login_Activity.class));
                                                }
                                            });
                                            builder1.create().show();
                                            ETnewpassfist.setEnabled(false);
                                            ETnewpasssecond.setEnabled(false);
                                        }
                                    }
                                    else {
                                        Toast.makeText(Password_Change_Activity.this,"Please Enter OTP",Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getIntent()));
                                    }
                                }
                            });
                            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!isLoginActivity) startActivity(new Intent(Password_Change_Activity.this,Homepage_Activity.class));
                                    else startActivity(new Intent(Password_Change_Activity.this,Login_Activity.class));
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            editText.requestFocus();
                        }
                        else {
                            Toast.makeText(Password_Change_Activity.this,"New passwords does not match",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Password_Change_Activity.this,"Incorrect PF number.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String finalmessage = message.replaceAll("[^0-9]", "");
                editText.setText(finalmessage);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isLoginActivity) startActivity(new Intent(Password_Change_Activity.this,Homepage_Activity.class));
                else startActivity(new Intent(Password_Change_Activity.this,Login_Activity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public Boolean checkPFNo(String pfno){
        BGSendMessage bgSendMessage = new BGSendMessage();
        try {
            String json = bgSendMessage.execute("", "", pfno, "CHECK1", "", "").get();
//                        Toast.makeText(Password_Change_Activity.this,json,Toast.LENGTH_SHORT).show();
            if (!json.equals("Error")) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                name = jsonObject1.getString("name");
                mobile = jsonObject1.getString("mobile");
                txtmessage = name + " your password has been succesfully changed.If you didn't do this contact you admin.";
                return true;
            } else {
                String color1 = "#f20000";
                ETpfno.setTextColor(Color.parseColor(color1));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changepass(String mobileno,String pfno){
        String newpassfirst = ETnewpassfist.getText().toString();
        String newpasssecond = ETnewpasssecond.getText().toString();

        BGSendMessage bgSendMessage = new BGSendMessage();

        if (newpassfirst.equals(newpasssecond) && !newpassfirst.isEmpty()){
            bgSendMessage.execute(mobileno,txtmessage,pfno,"UPDATE",newpassfirst,"");
            AlertDialog.Builder builder = new AlertDialog.Builder(Password_Change_Activity.this);
            builder.setTitle("Action Taken Succesfully");
            builder.setMessage("Your password has been succesfully changed.\n Please login again for security reasons.");
            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Homepage_Activity.logout(Password_Change_Activity.this);
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
        if (!newpassfirst.equals(newpasssecond)){
            Toast.makeText(Password_Change_Activity.this,"New passwords does not match",Toast.LENGTH_SHORT).show();
        }
    }

    public static class BGSendMessage extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
//            String send_message_url = "http://192.168.0.101/leavemodule/SendMessage.php";
            String send_message_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/SendMessage.php";
//            String send_message_url = "http://10.159.22.53/leavemodule/SendMessage.php";
            String mobile = params[0];
            String message = params[1];
            String pfno = params[2];
            String type = params[3];
            String newpass = params[4];
            String oldpass = params[5];
            try {
                URL url = new URL(send_message_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                OutputStream outputStream =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("mobile","UTF-8") + "=" + URLEncoder.encode(mobile,"UTF-8") + "&" +
                        URLEncoder.encode("message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8") + "&" +
                        URLEncoder.encode("pfno","UTF-8") + "=" + URLEncoder.encode(pfno,"UTF-8") + "&" +
                        URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode(type,"UTF-8") + "&" +
                        URLEncoder.encode("newpass","UTF-8") + "=" + URLEncoder.encode(newpass,"UTF-8") + "&" +
                        URLEncoder.encode("oldpass","UTF-8") + "=" + URLEncoder.encode(oldpass,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String json = "";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    json += line;
                }
                bufferedReader.close();
                httpURLConnection.disconnect();
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class MySMSBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsm = null;
            String sms_str ="";

            if (bundle != null)
            {
                Object[] pdus = (Object[]) bundle.get("pdus");
                smsm = pdus != null ? new SmsMessage[pdus.length] : new SmsMessage[0];
                for (int i=0; i<smsm.length; i++){
                    smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    sms_str += smsm[i].getMessageBody();

                    String sender = smsm[i].getOriginatingAddress().substring(3);
                    //Check here sender is yours
                    Intent smsIntent = new Intent("otp");
                    if (sender.equals("FINSUR")) {
                        smsIntent.putExtra("message", sms_str);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                    }
                }
            }
        }
    }
}