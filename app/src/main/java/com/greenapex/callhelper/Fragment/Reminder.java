package com.greenapex.callhelper.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.greenapex.callhelper.Activity.ReminderCreate;
import com.greenapex.callhelper.Adpter.adpterReminder;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.Model.contactReminder;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.util.List;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reminder extends Fragment {

    FloatingActionButton fabReminder;
    RecyclerView recyclerReminder;
    SharedPreferences preferences;
    MyDBHandler dbHandler;
    adpterReminder adpter;

    public static Reminder newInstance() {
        Reminder fragment = new Reminder();
        return fragment;
    }

    public Reminder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        dbHandler = new MyDBHandler(getContext());
        fabReminder = (FloatingActionButton) view.findViewById(R.id.fabReminder);
        recyclerReminder = (RecyclerView) view.findViewById(R.id.recyclerReminder);
        fabReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("contactNumber");
                editor.remove("contactName");
                editor.remove("contactNumber1");
                editor.remove("contactName1");
                editor.commit();
                startActivity(new Intent(getActivity(), ReminderCreate.class));
            }
        });
        reminder();
        return view;
    }

    public void reminder() {

        //change for ball detail
        List<contactNote> list = dbHandler.selectAll("reminder");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerReminder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adpter = new adpterReminder(getContext(), list);
        recyclerReminder.setAdapter(adpter);
        adpter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        reminder();
    }
}
