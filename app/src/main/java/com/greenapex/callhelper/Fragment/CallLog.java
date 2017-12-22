package com.greenapex.callhelper.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenapex.callhelper.Adpter.AdpterLog;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallLog extends Fragment {

    RecyclerView recyclerView;
    ArrayList<conctactPojo> mItems = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    Cursor managedCursor;

    public static CallLog newInstance() {
        CallLog fragment = new CallLog();
        return fragment;
    }

    public CallLog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.log_recycler);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else {
            getCallDetails();
        }
        return view;
    }

    private void getCallDetails() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managedCursor = getContext().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);
        int name = managedCursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);

        while (managedCursor.moveToNext()) {
            String callName = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
            String dateString = formatter.format(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case android.provider.CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
                case android.provider.CallLog.Calls.REJECTED_TYPE:
                    dir = "REJECTED";
                    break;
            }


            conctactPojo callLogItem = new conctactPojo();
            callLogItem.setContactName(callName);
            callLogItem.setContactNumber(phNumber);
            callLogItem.setCallDate(dateString);
           // callLogItem.setCallDuration(callDuration);
            callLogItem.setCallType(dir);
            mItems.add(callLogItem);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            mAdapter = new AdpterLog(getContext(), mItems);
            recyclerView.setAdapter(mAdapter);
        }
        managedCursor.close();
        return;

    }

}


