package com.example.mimohkulkarni.leavemodule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
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

public class LocalDatabase extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "office.db";
    //    private static final String TABLE_NAME1 = "perinfo1";
    private static final String TABLE_NAME = "perinfo";
    private static final String COL_PF_NO = "pfno";
    private static final String COL_NAME = "name";
    private static final String COL_PAN_NO = "panno";
    private static final String COL_MOBILE = "mobile";
    private static final String COL_DESG = "desg";
    private static final String COL_BDATE = "bdate";
    private static final String COL_STATION = "station";
    private static final String COL_DEPT = "dept";
    private static final String COL_LEVEL = "level";
    private static final String COL_AP_DATE = "apdate";
    private static final String COL_DSEC_DATE = "dsec";
    private static final String COL_VIIPAY = "viipay";
    private static final String COL_VIILVL = "viilvl";
    private static final String COL_SEC = "sec";
    private static final String COL_CATG = "catg";
    private static final String COL_NEXTINC = "nextinc";
    private static final String COL_PENDING = "pending";
//    private static final String COL_SANCTIONED = "sanctioned";
//    private static final String COL_BDATE_CHECK = "bdatecheck";
    private int number;

    private static Context context;

    public LocalDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_PF_NO + " VARCHAR," + COL_NAME + " VARCHAR," + COL_PAN_NO + " VARCHAR,"
                + COL_MOBILE + " VARCHAR," + COL_DESG + " VARCHAR," + COL_BDATE + " VARCHAR," + COL_STATION + " VARCHAR,"
                + COL_DEPT + " VARCHAR," + COL_LEVEL + " VARCHAR," + COL_AP_DATE + " VARCHAR," + COL_DSEC_DATE + " VARCHAR,"
                + COL_VIIPAY + " VARCHAR," + COL_VIILVL + " VARCHAR," + COL_SEC + " VARCHAR," + COL_CATG + " VARCHAR,"
                + COL_NEXTINC + " VARCHAR," + COL_PENDING + " VARCHAR)");
    }

    public Boolean adduser(final HashMap<String,String> hashMap){

//        String show_leaves_url = "http://192.168.0.105/leavemodule/ShowLeaveList.php";
//        String show_leaves_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/ShowLeaveList.php";
        String show_leaves_url = "http://10.159.22.53/leavemodule/ShowLeaveList.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_leaves_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//              Toast.makeText(Login_Activity.this,response,Toast.LENGTH_SHORT).show();
                try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("resultarray");
                number = jsonArray.length();
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
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PF_NO,hashMap.get("pfno"));
        contentValues.put(COL_NAME,hashMap.get("name"));
        contentValues.put(COL_PAN_NO,hashMap.get("panno"));
        contentValues.put(COL_MOBILE,hashMap.get("mobile"));
        contentValues.put(COL_DESG,hashMap.get("desg"));
        contentValues.put(COL_BDATE,hashMap.get("bdate"));
        contentValues.put(COL_STATION,hashMap.get("station"));
        contentValues.put(COL_DEPT,hashMap.get("dept"));
        contentValues.put(COL_LEVEL,hashMap.get("level"));
        contentValues.put(COL_AP_DATE,hashMap.get("apdate"));
        contentValues.put(COL_DSEC_DATE,hashMap.get("dsec"));
        contentValues.put(COL_VIIPAY,hashMap.get("viipay"));
        contentValues.put(COL_VIILVL,hashMap.get("viilvl"));
        contentValues.put(COL_SEC,hashMap.get("sec"));
        contentValues.put(COL_CATG,hashMap.get("catg"));
        contentValues.put(COL_NEXTINC,hashMap.get("nextinc"));
        contentValues.put(COL_PENDING,String.valueOf(number));

        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
        return result != -1;
    }

    public HashMap<String,String> getuserdata(){
        HashMap<String,String> hashMap = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            hashMap.put(COL_PF_NO,cursor.getString(0));
            hashMap.put(COL_NAME,cursor.getString(1));
            hashMap.put(COL_PAN_NO,cursor.getString(2));
            hashMap.put(COL_MOBILE,cursor.getString(3));
            hashMap.put(COL_DESG,cursor.getString(4));
            hashMap.put(COL_BDATE,cursor.getString(5));
            hashMap.put(COL_STATION,cursor.getString(6));
            hashMap.put(COL_DEPT,cursor.getString(7));
            hashMap.put(COL_LEVEL,cursor.getString(8));
            hashMap.put(COL_AP_DATE,cursor.getString(9));
            hashMap.put(COL_DSEC_DATE,cursor.getString(10));
            hashMap.put(COL_VIIPAY,cursor.getString(11));
            hashMap.put(COL_VIILVL,cursor.getString(12));
            hashMap.put(COL_SEC,cursor.getString(13));
            hashMap.put(COL_CATG,cursor.getString(14));
            hashMap.put(COL_NEXTINC,cursor.getString(15));
            hashMap.put(COL_PENDING,cursor.getString(16));
        }
        cursor.close();
        sqLiteDatabase.close();
        return hashMap;
    }

    public void updatenumber(String number){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PENDING ,number);
        sqLiteDatabase.update(TABLE_NAME,contentValues,COL_PENDING + " = ?",new String[]{COL_PENDING});
    }

    public void deleteuser(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,null,null);
        sqLiteDatabase.close();
    }
}