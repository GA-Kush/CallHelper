package com.greenapex.callhelper.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.greenapex.callhelper.Adpter.adpterContact;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactList extends Fragment {

    RecyclerView recyclerView;
    TextView txtMsg;
    ArrayList<conctactPojo> mItems = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    Cursor cursor;

    public static ContactList newInstance() {
        ContactList fragment = new ContactList();
        return fragment;
    }

    public ContactList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        } else {
            //Do stuff
            contact_detail();
        }
        if (ContextCompat.checkSelfPermission((Activity) getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                ActivityCompat.requestPermissions((Activity) getContext(),
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
            //do nothing
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                        //Do stuff

                        contact_detail();

                    }
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {


                    } else {
                        Toast.makeText(getContext(), "No Permission Granted!!", Toast.LENGTH_SHORT).show();
                    }


                    return;
                }
            }
        }

    }

    public void contact_detail() {
        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        int name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        while (cursor.moveToNext()) {
            String callName = cursor.getString(name);
            String phNumber = cursor.getString(number);
            phNumber = phNumber.replaceAll("\\(", "");
            phNumber = phNumber.replaceAll("\\) ", "");
            phNumber = phNumber.replaceAll("\\-", "");
            conctactPojo callLogItem = new conctactPojo();
            callLogItem.setContactName(callName);
            callLogItem.setContactNumber(phNumber);

            mItems.add(callLogItem);
            if (mItems.isEmpty()) {
                txtMsg.setVisibility(View.VISIBLE);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            mAdapter = new adpterContact(getContext(), mItems);
            recyclerView.setAdapter(mAdapter);
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
