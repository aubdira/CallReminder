package com.example.aub.callreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import java.util.List;


/**
 * Created by aub on 12/21/17.
 * Project: CallReminder
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts_info WHERE id = :id")
    Maybe<Contact> getContactById(int id);

    @Query("SELECT * FROM contacts_info WHERE is_log = 0 ORDER BY reminder_time ASC")
    Flowable<List<Contact>> getContactsListByTimeASC();

    @Query("SELECT * FROM contacts_info WHERE is_log = 1 ORDER BY reminder_time DESC")
    Flowable<List<Contact>> getContactsLogListByTimeDESC();

    @Insert long insertContact(Contact contact);

    @Query("UPDATE contacts_info SET is_log = 1 WHERE reminder_time = :time")
    int updateContactAsLog(long time);

    @Delete void deleteContact(Contact contact);
}
