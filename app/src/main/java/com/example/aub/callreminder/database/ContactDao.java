package com.example.aub.callreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;


/**
 * Created by aub on 12/21/17.
 * Project: CallReminder
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts_info WHERE id = :id")
    Contact getContactById(int id);

    @Query("SELECT * FROM contacts_info WHERE is_log = 0 ORDER BY reminder_time ASC")
    List<Contact> getContactsListByTimeASC();

    @Query("SELECT * FROM contacts_info WHERE is_log = 1 ORDER BY reminder_time ASC")
    List<Contact> getContactsLogListByTimeASC();

    @Query("SELECT * FROM contacts_info WHERE reminder_time = :time")
    Contact getContactByTime(long time);

    @Insert long insertContact(Contact contact);

    @Delete void deleteContact(Contact contact);

    @Update void updateContactAsLog(Contact contact);
}
