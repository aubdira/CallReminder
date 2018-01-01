package com.example.aub.callreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;


/**
 * Created by aub on 12/21/17.
 * Project: CallReminder
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts_info")
    List<Contact> getContactsList();

    @Query("SELECT * FROM contacts_info WHERE id = :id")
    Contact getContactById(int id);

    @Query("SELECT * FROM contacts_info ORDER BY reminder_time DESC")
    List<Contact> getContactsListByTimeDESC();

    @Query("SELECT * FROM contacts_info ORDER BY reminder_time ASC")
    List<Contact> getContactsListByTimeASC();

    @Insert long insertContact(Contact contact);

    @Delete void deleteContact(Contact contact);
}
