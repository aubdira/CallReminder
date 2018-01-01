package com.example.aub.callreminder;

import android.app.Application;
import android.content.Context;


/**
 * Created by aub on 12/23/17.
 * Project: CallReminder
 */

public class App extends Application {

    private static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
