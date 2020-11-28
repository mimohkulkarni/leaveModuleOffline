package com.example.mimohkulkarni.leavemodule;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BGPendingBroadcast extends BroadcastReceiver {
    LocalDatabase localDatabase;
    int onMethod;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isRemember = myPrefs.getBoolean("RememberMe", false);
        boolean noti = myPrefs.getBoolean("notification", false);
        localDatabase = new LocalDatabase(context);
        final HashMap<String, String> hashMap = localDatabase.getuserdata();

//        String show_leaves_url = "http://192.168.0.105/leavemodule/ShowLeaveList.php";
//        String show_leaves_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/ShowLeaveList.php";
        String show_leaves_url = "http://10.159.22.53/leavemodule/ShowLeaveList.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_leaves_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                onMethod = jsonArray.length();
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
                map.put("pfno",hashMap.get("pfno"));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        if (isRemember && noti) {
            if (Integer.valueOf(hashMap.get("pending")) > 0) {
                Intent noti_intent = new Intent(context, Homepage_Activity.class);
                noti_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                noti_intent.putExtra("isNotification",true);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, noti_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                String text = "You have " + String.valueOf(onMethod) + " pending leaves to take decisions";
                String title = "Action Required";
                showNotification(context, text, title, pendingIntent);
            }
            DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);
            DateFormat outputFormat = new SimpleDateFormat("dd-MM",Locale.US);
//            String inputDateStr = "16-Apr-2018";
            String inputDateStr = hashMap.get("bdate");
            Date date = null;
            try {
                date = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String inputDateString = outputFormat.format(date);
//            String month = inputDateString.substring(3,5);
//            String startdate = inputDateString.substring(0,2);
//            String finaldate,finalmonth;
//            if (month.substring(0,1).equals("0")) finalmonth = month.substring(1,2);
//            else finalmonth = month;
//            if (startdate.substring(0,1).equals("0")) finaldate = startdate.substring(1,2);
//            else finaldate = startdate;
            Calendar calendar = Calendar.getInstance();
            Date checkDate = calendar.getTime();
            String checkDateString = outputFormat.format(checkDate);

            if(checkDateString.equals(inputDateString)) {
                String text = "Happy Birthday!!!!!! Have a marvelous day!!!";
                String title = "Happ Birthday " + hashMap.get("name");
                Intent noti_intent = new Intent(context, null);
                noti_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, noti_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                showNotification(context,text,title,pendingIntent);
            }
        }
        else {
            String title = "Action Required";
            String text = "Stay login. So we can notify you about status of leaves";
            showNotification(context, text, title, null);
        }
    }

    public void showNotification(Context context,String text,String title,PendingIntent pendingIntent){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.mipmap.leavemodule_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[] { 750, 750 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, mBuilder.build());
        }
    }
}