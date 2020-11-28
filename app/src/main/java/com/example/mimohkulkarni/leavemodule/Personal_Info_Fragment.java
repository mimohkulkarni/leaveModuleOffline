package com.example.mimohkulkarni.leavemodule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Objects;

public class Personal_Info_Fragment extends Fragment {

    LocalDatabase localDatabase;

    ImageView profile_photo;
    TextView TVdisplay_pfno,TVdisplay_name,TVdisplay_panno,TVdisplay_mobile,TVdisplay_desg,TVdisplay_bdate,
            TVdisplay_station,TVdisplay_dept,TVdisplay_apdate,TVdisplay_dsec,TVdisplay_viipay,TVdisplay_viilvl,
            TVdisplay_sec,TVdisplay_catg,TVdisplay_nxtinc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        TVdisplay_pfno = view.findViewById(R.id.TVdisplay_pfno);
        TVdisplay_name = view.findViewById(R.id.TVdisplay_name);
        TVdisplay_panno = view.findViewById(R.id.TVdisplay_panno);
        TVdisplay_mobile = view.findViewById(R.id.TVdisplay_mobile);
        TVdisplay_desg = view.findViewById(R.id.TVdisplay_desg);
        TVdisplay_bdate = view.findViewById(R.id.TVdisplay_bdate);
        TVdisplay_station = view.findViewById(R.id.TVdisplay_station);
        TVdisplay_dept = view.findViewById(R.id.TVdisplay_dept);
        TVdisplay_apdate = view.findViewById(R.id.TVdisplay_apdate);
        TVdisplay_dsec = view.findViewById(R.id.TVdisplay_dsec);
        TVdisplay_viipay = view.findViewById(R.id.TVdisplay_viipay);
        TVdisplay_viilvl = view.findViewById(R.id.TVdisplay_viilvl);
        TVdisplay_sec = view.findViewById(R.id.TVdisplay_sec);
        TVdisplay_catg = view.findViewById(R.id.TVdisplay_catg);
        TVdisplay_nxtinc = view.findViewById(R.id.TVdisplay_nxtinc);
        profile_photo = view.findViewById(R.id.profile_photo);

        localDatabase = new LocalDatabase(getContext());
        HashMap<String,String> hashMap = localDatabase.getuserdata();

        TVdisplay_pfno.setText(hashMap.get("pfno"));
        TVdisplay_name.setText(hashMap.get("name"));
        TVdisplay_panno.setText(hashMap.get("panno"));
        TVdisplay_mobile.setText(hashMap.get("mobile"));
        TVdisplay_desg.setText(hashMap.get("desg"));
        TVdisplay_bdate.setText(hashMap.get("bdate"));
        TVdisplay_station.setText(hashMap.get("station"));
        TVdisplay_dept.setText(hashMap.get("dept"));
        TVdisplay_apdate.setText(hashMap.get("apdate"));
        TVdisplay_dsec.setText(hashMap.get("dsec"));
        TVdisplay_viipay.setText(hashMap.get("viipay"));
        TVdisplay_viilvl.setText(hashMap.get("viilvl"));
        TVdisplay_sec.setText(hashMap.get("sec"));
        TVdisplay_catg.setText(hashMap.get("catg"));
        TVdisplay_nxtinc.setText(hashMap.get("nextinc"));

//        String image_url = "http://leave-module-by-mimoh-kulkarni.000webhostapp.com/profile_photos/a"+hashMap.get("pfno")+".jpg";
//        ImageRequest imageRequest = new ImageRequest(image_url, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap response) {
//                profile_photo.setImageBitmap(response);
//            }
//        }, 150, 150, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(),"Image Not Found",Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
//            }
//        });
//        MySingleton.getInstance(getContext()).addToRequestQueue(imageRequest);

        String PACKAGE_NAME = Objects.requireNonNull(getActivity()).getPackageName();
        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/a"+hashMap.get("pfno") ,null,null);
        profile_photo.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));

        return view;
    }
}
