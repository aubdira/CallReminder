package com.example.aub.callreminder.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by aub on 12/22/17.
 * Project: CallReminder
 */

public class DateTimeConverter {

    private static Calendar sCal;

    public static long getTimeInMills(int year, int month, int day, int hour, int minute) {
        sCal = Calendar.getInstance();
        sCal.set(Calendar.YEAR, year);
        sCal.set(Calendar.MONTH, month);
        sCal.set(Calendar.DAY_OF_MONTH, day);
        sCal.set(Calendar.HOUR_OF_DAY, hour);
        sCal.set(Calendar.MINUTE, minute);
        sCal.set(Calendar.SECOND, 0);
        sCal.set(Calendar.MILLISECOND, 0);
        return sCal.getTimeInMillis();
    }

    public static long getNowDate() {
        sCal = Calendar.getInstance();
        return sCal.getTimeInMillis();
    }

    public static String getFormattedDateTime(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm   dd/MM/yy", Locale.getDefault());
        sCal = Calendar.getInstance();
        sCal.setTimeInMillis(millis);
        return formatter.format(sCal.getTime());
    }
}
