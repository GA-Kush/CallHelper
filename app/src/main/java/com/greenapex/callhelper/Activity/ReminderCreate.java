package com.greenapex.callhelper.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.CommonUtils;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

public class ReminderCreate extends AppCompatActivity {

    Button btnClose, btnSave, btnContact, btnDate, btnTime;
    SharedPreferences preferences;
    int countText = 0;
    TextView txtNumber, txtSetDate, txtSetTime, txtTextNoteLimit;
    EditText edtNote;
    String contactNumber, contactName, cNote;
    Calendar myCalendar;
    MyDBHandler dbHandler;
    Intent intent;
    String popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create);
        preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        dbHandler = new MyDBHandler(ReminderCreate.this);
        btnDate = (Button) findViewById(R.id.btnReminderSetDate);
        btnTime = (Button) findViewById(R.id.btnReminderSetTime);
        btnClose = (Button) findViewById(R.id.btnReminderClose);
        btnSave = (Button) findViewById(R.id.btnReminderSave);
        btnContact = (Button) findViewById(R.id.btnReminderSelectContact);
        txtNumber = (TextView) findViewById(R.id.txtReminderSelectContact);
        txtSetDate = (TextView) findViewById(R.id.txtReminderSetDate);
        txtSetTime = (TextView) findViewById(R.id.txtReminderSetTime);
        txtTextNoteLimit = (TextView) findViewById(R.id.txtReminderNoteLimit);
        edtNote = (EditText) findViewById(R.id.edtReminderWriteNote);
        myCalendar = Calendar.getInstance();
        intent = getIntent();
        popup = intent.getStringExtra("popup");

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactList.class));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date1 = null;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                Date date = new Date();
                String Date = String.valueOf(dateFormat.format(date));
                String cDate = txtSetDate.getText().toString();
                String cTime = txtSetTime.getText().toString();
                String compareDateTime = "" + cDate + " " + cTime;
                SimpleDateFormat dateSpecified = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                try {
                    date1 = dateSpecified.parse(compareDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("Date", "Current Date:" + Date + "Compare Date:" + compareDateTime);


                if (txtNumber.getText().toString().equals("")) {
                    Toast.makeText(ReminderCreate.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                } else if (txtSetDate.getText().toString().equals("")) {
                    Toast.makeText(ReminderCreate.this, "Please Enter Set Date", Toast.LENGTH_SHORT).show();

                } else if (txtSetTime.getText().toString().equals("")) {
                    Toast.makeText(ReminderCreate.this, "Please Enter Set Time", Toast.LENGTH_SHORT).show();
                } else if (edtNote.getText().toString().equals("")) {
                    Toast.makeText(ReminderCreate.this, "Please Enter Set Note", Toast.LENGTH_SHORT).show();
                } else if (date1.before(date)) {
                    Toast.makeText(ReminderCreate.this, "Please Enter Set perfect Date And Time", Toast.LENGTH_SHORT).show();
                } else {
                    addReminder();
                    txtNumber.setText("");
                    txtSetDate.setText("");
                    txtSetTime.setText("");
                    edtNote.setText("");
                    finish();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNumber.setText("");
                txtSetDate.setText("");
                txtSetTime.setText("");
                edtNote.setText("");
                finish();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTime();
            }
        };

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(ReminderCreate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        txtSetDate.setText("" + year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ReminderCreate.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //text Erase then effect
                countText = countText - count;
                txtTextNoteLimit.setText(countText + "/200");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //text Add then effect
                countText = count + countText;
                if (countText == 200) {
                    Toast.makeText(ReminderCreate.this, "full", Toast.LENGTH_SHORT).show();
                    Log.d("text", "full" + countText);
                }
                txtTextNoteLimit.setText(countText + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        edtNote.addTextChangedListener(textWatcher);
        txtTextNoteLimit.setText(countText + "/200");


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        contactNumber = preferences.getString("contactNumber1", null);
        contactName = preferences.getString("contactName1", null);
        if (contactName != null && contactNumber != null)
            txtNumber.setText("" + contactName + "(" + contactNumber + ")");
        else
            txtNumber.setText("");

        if (popup != null && popup.equalsIgnoreCase("popup")) {
            contactNumber = intent.getStringExtra("contactNumber");
            contactName = intent.getStringExtra("contactName");
            txtNumber.setText("" + contactName + "(" + contactNumber + ")");
            btnContact.setVisibility(View.GONE);
        }
    }

    private void updateTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtSetTime.setText(sdf.format(myCalendar.getTime()));
    }

    public void addReminder() {
        cNote = edtNote.getText().toString();

        contactNote bean = new contactNote();
        bean.setContactNumber(contactNumber);
        bean.setContactName(contactName);
        bean.setContactNote(cNote);
        bean.setContactNoteDateTime(CommonUtils.getDateTime());
        bean.setTime(txtSetTime.getText().toString());
        bean.setDate(txtSetDate.getText().toString());
        bean.setType("reminder");
        Log.d("Reminder", " Number " + bean.getContactNumber()
                + " Name " + bean.getContactName()
                + " Note " + bean.getContactNote()
                + " DATETIME " + bean.getContactNoteDateTime()
                + " DATE " + bean.getDate()
                + " TIME " + bean.getTime());
        dbHandler.addNote(bean);
    }
}
