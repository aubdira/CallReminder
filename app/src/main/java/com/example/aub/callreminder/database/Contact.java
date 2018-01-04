package com.example.aub.callreminder.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


/**
 * Created by aub on 12/21/17.
 * Project: CallReminder
 */
@Entity(tableName = "contacts_info")
public class Contact {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "contact_name")
    private String contactName;

    @ColumnInfo(name = "contact_phone_number")
    private String contactPhoneNumber;

    @ColumnInfo(name = "reminder_time")
    private long reminderTime;

    @ColumnInfo(name = "reminder_reason")
    private String reminderReason;

    @ColumnInfo(name = "is_log")
    private int isLog = 0;

    /*
        Constructor
     */

    public Contact(String contactName, String contactPhoneNumber, long reminderTime,
            String reminderReason) {
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
        this.reminderTime = reminderTime;
        this.reminderReason = reminderReason;
    }

    /*
        Getters and Setters
     */


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderReason() {
        return reminderReason;
    }

    public void setReminderReason(String reminderReason) {
        this.reminderReason = reminderReason;
    }


    public int getIsLog() {
        return isLog;
    }

    public void setIsLog(int isLog) {
        this.isLog = isLog;
    }
}
