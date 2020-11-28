package com.example.mimohkulkarni.leavemodule;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings_Activity extends AppCompatActivity{

    CheckBox noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        noti = findViewById(R.id.opennoti);

        SharedPreferences notification = Settings_Activity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean notificationBoolean = notification.getBoolean("notification", false);
        if (notificationBoolean) noti.setChecked(true);
        else noti.setChecked(false);

        noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences notification = Settings_Activity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = notification.edit();
                if (noti.isChecked())   prefsEditor.putBoolean("notification",true);
                else prefsEditor.putBoolean("notification",false);
                prefsEditor.apply();
            }
        });

    }
}
