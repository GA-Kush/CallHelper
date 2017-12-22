package com.greenapex.callhelper;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.greenapex.callhelper.Activity.Setting;
import com.greenapex.callhelper.Fragment.CallLog;
import com.greenapex.callhelper.Fragment.CallNote;
import com.greenapex.callhelper.Fragment.ContactList;
import com.greenapex.callhelper.Fragment.Reminder;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.Service.AlarmReceiver;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    Button btnSetting;
    MyDBHandler dbHandler;
    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new MyDBHandler(getApplicationContext());
        btnSetting = (Button) findViewById(R.id.btnSetting);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlarmStorage();
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Setting.class));
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menuNote:
                        selectedFragment = CallNote.newInstance();

                        break;
                    case R.id.menuReminder:
                        selectedFragment = Reminder.newInstance();
                        break;
                    case R.id.menuContact:
                        selectedFragment = ContactList.newInstance();
                        break;
                    case R.id.menu_callHistory:
                        selectedFragment = CallLog.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, CallLog.newInstance());
        transaction.commit();
    }

    static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void AlarmStorage() {

        List<contactNote> contactNotes = dbHandler.reminderTime();
        for (contactNote notes : contactNotes) {

            String Date = notes.getDate();
            String Time = notes.getTime();
            String ContactName = notes.getContactName();
            String ContactNumber = notes.getContactNumber();
            String ContactNote = notes.getContactNote();
            String reminderDate = Date + " " + Time;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date = null;
            try {
                date = dateFormat.parse(reminderDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal_alarm = Calendar.getInstance();
            cal_alarm.setTime(date);
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.setData(Uri.parse(reminderDate));
            final int _id = (int) date.getTime();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(
                    cal_alarm.getTimeInMillis(),
                    pendingIntent);

            //alarmManager.setAlarmClock(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo,  pendingIntent);

        }
    }
}
