package com.example.mimohkulkarni.leavemodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class Leave_Fragment extends Fragment implements View.OnClickListener{

    Button btnleaveapp,btnleavebal,btnsanctionleave,btnleavestatus;

    Leave_Application_Fragment leave_application_fragment = new Leave_Application_Fragment();
    Leave_Balance_Fragment leave_balance_fragment = new Leave_Balance_Fragment();
    Select_Sanction_Fragment select_sanction_fragment = new Select_Sanction_Fragment();
    Leave_Status_Fragment leave_status_fragment = new Leave_Status_Fragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);

        btnleaveapp = view.findViewById(R.id.btnleaveapp);
        btnleavebal = view.findViewById(R.id.btnleavebal);
        btnsanctionleave = view.findViewById(R.id.btnsanctionleave);
        btnleavestatus = view.findViewById(R.id.btnleavestatus);

        btnsanctionleave.setOnClickListener(Leave_Fragment.this);
        btnleaveapp.setOnClickListener(Leave_Fragment.this);
        btnleavebal.setOnClickListener(Leave_Fragment.this);
        btnleavestatus.setOnClickListener(Leave_Fragment.this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnleaveapp:
//                Homepage_Activity homepage_activity = new Homepage_Activity();
//                homepage_activity.addFragment(leave_application_fragment);
                replaceFragment(leave_application_fragment);
                break;

            case R.id.btnleavebal:
                replaceFragment(leave_balance_fragment);
                break;

            case R.id.btnsanctionleave:
                replaceFragment(select_sanction_fragment);
                break;

            case R.id.btnleavestatus:
                replaceFragment(leave_status_fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
