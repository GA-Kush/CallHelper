package com.greenapex.callhelper.Activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.greenapex.callhelper.Adpter.adpterContactSelect;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtMsg;
    Button btnDone;
    ArrayList<conctactPojo> mItems = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    SharedPreferences preferences;
    public static final String PREFS_NAME = "contactList";
    Cursor cursor;
    String contactNumber, contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        recyclerView = (RecyclerView) findViewById(R.id.contactList_recycler);
        btnDone = (Button) findViewById(R.id.btnDone);
        txtMsg = (TextView) findViewById(R.id.txt_ContactListMsg);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();

                contactNumber = preferences.getString("contactNumber", "");
                contactName = preferences.getString("contactName", "");
                editor.putString("contactNumber1", contactNumber);
                editor.putString("contactName1", contactName);
                editor.commit();
                finish();
            }
        });

        contact_detail();
    }

    public void contact_detail() {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

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

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            mAdapter = new adpterContactSelect(getApplicationContext(), mItems);
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
