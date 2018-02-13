package com.example.aub.callreminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.aub.callreminder.App;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.events.NotifyMeEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by aub on 2/6/18.
 * Project: CallReminder
 */

public class NotificationCreator {

    @Inject
    Context context;

    public NotificationCreator() {
        App.getContactRepositoryComponent().inject(this);
    }

    public void remindUserToCall(String contactName, String contactPhoneNumber,
            String reminderReason, long timeInMillis) {

        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NAME, contactName);
        intent.putExtra(NotificationPublisher.PHONE, contactPhoneNumber);
        intent.putExtra(NotificationPublisher.REASON, reminderReason);
        intent.putExtra(NotificationPublisher.TIME, timeInMillis);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) timeInMillis,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }

        NotifyMeEvent event = new NotifyMeEvent();
        EventBus.getDefault().post(event);
    }
}
