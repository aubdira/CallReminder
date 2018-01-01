package com.example.aub.callreminder.database;

import android.arch.persistence.room.Room;
import android.content.Context;


/**
 * Created by aub on 12/22/17. Project: CallReminder
 *
 * see https://github.com/srinurp/AndroidRoom/blob/master/app/src/main/java/com/zoftino/room/DatabaseCreator.java
 */

public class DatabaseCreator {

    private static AppDatabase appDatabase;
    private static final Object LOCK = new Object();

    public synchronized static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            synchronized (LOCK) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            AppDatabase.DB_NAME).build();
                }
            }
        }
        return appDatabase;
    }
}
