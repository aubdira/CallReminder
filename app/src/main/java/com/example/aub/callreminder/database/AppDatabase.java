package com.example.aub.callreminder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by aub on 12/22/17.
 * Project: CallReminder
 */

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "contact_database";

    public abstract ContactDao getContactDao();
}
