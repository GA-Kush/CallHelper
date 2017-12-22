package com.greenapex.callhelper.Model;

/**
 * Created by pc01 on 30/11/17.
 */

public class contactReminder {

    int contactId;
    String contactNumber;
    String contactName;
    String contactNote;
    String contactNoteDateTime;
    String date;
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactNoteDateTime() {
        return contactNoteDateTime;
    }

    public void setContactNoteDateTime(String contactNoteDateTime) {
        this.contactNoteDateTime = contactNoteDateTime;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNote() {
        return contactNote;
    }

    public void setContactNote(String contactNote) {
        this.contactNote = contactNote;
    }
}
