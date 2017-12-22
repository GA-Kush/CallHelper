package com.greenapex.callhelper.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.CommonUtils;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

public class NoteCreate extends AppCompatActivity {

    EditText edtNote;
    Button btnClose, btnSave;
    int countText = 0;
    int len = 0;
    TextView txtTextNoteLimit, edtNumber;
    Button btnContact;
    SharedPreferences preferences;
    String contactNumber, contactName, contactNote;
    MyDBHandler dbHandler;
    String edtContactName, edtContactNote, edt, edtContactNumber;
    int edtContactId;
    contactNote bean;
    String popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        edtNumber = (TextView) findViewById(R.id.txtSelectContact);
        edtNote = (EditText) findViewById(R.id.edtWriteNote);
        txtTextNoteLimit = (TextView) findViewById(R.id.txtNoteLimit);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnContact = (Button) findViewById(R.id.btnSelectContact);
        dbHandler = new MyDBHandler(NoteCreate.this);
        contactNote = edtNote.getText().toString();

        contactNumber = preferences.getString("contactNumber1", null);
        contactName = preferences.getString("contactName1", null);
        if (contactName != null && contactNumber != null)
            edtNumber.setText("" + contactName + "(" + contactNumber + ")");
        else
            edtNumber.setText("");

        Intent intent = getIntent();

        edtContactId = intent.getIntExtra("contactId", 0);
        edt = intent.getStringExtra("note");
        txtTextNoteLimit.setText(len + "/200");
        popup = intent.getStringExtra("popup");




        if (edt != null && edt.equalsIgnoreCase("editNote")) {
            bean = dbHandler.singleNote(edtContactId);
            edtContactNote = bean.getContactNote();
            edtContactNumber = bean.getContactNumber();
            edtContactName = bean.getContactName();
            btnContact.setVisibility(View.GONE);
            edtNote.setText(edtContactNote);
            edtNumber.setText("" + edtContactName + "(" + edtContactNumber + ")");
            len = edtNote.getText().length();
            txtTextNoteLimit.setText(len + "/200");
        }

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactList.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtNumber.getText().toString().equals("")) {
                    Toast.makeText(NoteCreate.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                } else if (edtNote.getText().toString().equals("")) {
                    Toast.makeText(NoteCreate.this, "Please Enter Note", Toast.LENGTH_SHORT).show();
                } else if (edt != null && edt.equalsIgnoreCase("editNote")) {
                    noteUpdate();
                    edtNote.setText("");
                    edtNumber.setText("");
                    finish();
                } else {
                    callWebService();
                    edtNote.setText("");
                    edtNumber.setText("");
                    finish();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtNote.setText("");
                edtNumber.setText("");
                finish();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //text Erase then effect
                len = len - count;
                txtTextNoteLimit.setText(len + "/200");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //text Add then effect
                len = count + len;
                if (len == 200) {
                    Toast.makeText(NoteCreate.this, "full", Toast.LENGTH_SHORT).show();
                    Log.d("text", "full" + len);
                }
                txtTextNoteLimit.setText(len + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edtNote.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        contactNumber = preferences.getString("contactNumber1", null);
        contactName = preferences.getString("contactName1", null);
        if (contactName != null && contactNumber != null)
            edtNumber.setText("" + contactName + "(" + contactNumber + ")");
        if (popup != null && popup.equalsIgnoreCase("popup")) {
            contactNumber = intent.getStringExtra("contactNumber");
            contactName = intent.getStringExtra("contactName");
            edtNumber.setText("" + contactName + "(" + contactNumber + ")");
            btnContact.setVisibility(View.GONE);
        }
    }

    public void callWebService() {
        contactNote = edtNote.getText().toString();

        bean = new contactNote();
        bean.setContactNumber(contactNumber);
        bean.setContactName(contactName);
        bean.setContactNote(contactNote);
        bean.setType("note");
        bean.setContactNoteDateTime(CommonUtils.getDateTime());
        Log.d("contact", "Number" + bean.getContactNumber()
                + "Name" + bean.getContactName()
                + "Note" + bean.getContactNote()
                + "DATETIME" + bean.getContactNoteDateTime());
        dbHandler.addNote(bean);
    }


    public void noteUpdate() {
        contactNote = edtNote.getText().toString();
        dbHandler.updateNote(contactNote, CommonUtils.getDateTime(), edtContactId);

    }


}
