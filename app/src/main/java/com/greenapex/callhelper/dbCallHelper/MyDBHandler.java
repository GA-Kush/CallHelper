package com.greenapex.callhelper.dbCallHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.greenapex.callhelper.Model.contactNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc01 on 30/11/17.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "callHelper.db";
    private static final String TABLE_NAME = "call_note_reminder";
    private static final String Id = "note_reminder_id";
    private static final String CONTACT_NUMBER = "contact_number";
    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_NOTE = "contact_note";
    private static final String CONTACT_NOTE_DATETIME = "contact_note_datetime";
    private static final String REMINDER_CONTACT_DATE = "reminder_date";
    private static final String REMINDER_CONTACT_TIME = "reminder_time";
    private static final String TYPE = "type";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = " CREATE TABLE " + TABLE_NAME + "("
                + Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTACT_NUMBER + " TEXT,"
                + CONTACT_NOTE + " TEXT,"
                + CONTACT_NAME + " TEXT,"
                + CONTACT_NOTE_DATETIME + " DATETIME DEFAULT (datetime('now','localtime')),"
                + REMINDER_CONTACT_DATE + " TEXT,"
                + REMINDER_CONTACT_TIME + " TEXT,"
                + TYPE + " TEXT" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNote(contactNote bean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_NUMBER, bean.getContactNumber());
        contentValues.put(CONTACT_NAME, bean.getContactName());
        contentValues.put(CONTACT_NOTE, bean.getContactNote());
        contentValues.put(CONTACT_NOTE_DATETIME, bean.getContactNoteDateTime());
        contentValues.put(REMINDER_CONTACT_TIME, bean.getTime());
        contentValues.put(REMINDER_CONTACT_DATE, bean.getDate());
        contentValues.put(TYPE, bean.getType());


        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public List<contactNote> selectAll(String value) {

        List<contactNote> runBeanList = new ArrayList<contactNote>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " LIKE '" + value + "' ORDER BY " + Id + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {

            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    contactNote bean = new contactNote();
                    bean.setContactId(cursor.getInt(cursor.getColumnIndex(Id)));
                    bean.setContactName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
                    bean.setContactNumber(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                    bean.setContactNote(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE)));
                    bean.setContactNoteDateTime(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE_DATETIME)));
                    bean.setTime(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_TIME)));
                    bean.setDate(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_DATE)));
                    bean.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                    runBeanList.add(bean);
                }
            }
        }

        return runBeanList;
    }

    public void deleteNote(int value) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = " DELETE FROM " + TABLE_NAME + " WHERE " + Id + " = " + value;
        database.execSQL(deleteQuery);
        database.close();
    }

    public void updateNote(String Note, String Dt, int value) {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = " UPDATE " + TABLE_NAME + " set " + CONTACT_NOTE + " = '" + Note + "'," + CONTACT_NOTE_DATETIME + " = '" + Dt + "' WHERE " + Id + " = " + value;
        database.execSQL(updateQuery);
        database.close();
    }

    public contactNote singleNote(int value) {

        contactNote runBeanList = new contactNote();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, Id + "=?", new String[]{String.valueOf(value)}, null, null, null);
        if (cursor != null) {

            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {

                    String Note = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE));
                    String contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME));
                    String contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER));

                    runBeanList.setContactNote(Note);
                    runBeanList.setContactName(contactName);
                    runBeanList.setContactNumber(contactNumber);
                }
            }
        }

        return runBeanList;
    }

    public List<contactNote> noteReminder(String value) {

        List<contactNote> runBeanList = new ArrayList<contactNote>();

        String query = " SELECT * FROM " + TABLE_NAME + " WHERE " + CONTACT_NUMBER + " = '" + value + "' ORDER BY " + Id + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    contactNote bean = new contactNote();
                    bean.setContactId(cursor.getInt(cursor.getColumnIndex(Id)));
                    bean.setContactName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
                    bean.setContactNumber(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                    bean.setContactNote(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE)));
                    bean.setContactNoteDateTime(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE_DATETIME)));
                    bean.setTime(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_TIME)));
                    bean.setDate(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_DATE)));
                    bean.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                    runBeanList.add(bean);
                }
            }
        }
        return runBeanList;
    }

   public List<contactNote> reminderTime(){
       List<contactNote> runBeanList = new ArrayList<contactNote>();
       String query = " SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " = 'reminder' ";
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(query, null);
       if (cursor != null){
           if (cursor.isBeforeFirst()){
               while (cursor.moveToNext()){
                   contactNote bean = new contactNote();
                   bean.setContactId(cursor.getInt(cursor.getColumnIndex(Id)));
                   bean.setContactName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
                   bean.setContactNumber(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                   bean.setContactNote(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE)));
                   bean.setContactNoteDateTime(cursor.getString(cursor.getColumnIndex(CONTACT_NOTE_DATETIME)));
                   bean.setTime(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_TIME)));
                   bean.setDate(cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_DATE)));
                   bean.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                   runBeanList.add(bean);
               }
           }
       }

       return runBeanList;
   }

}
