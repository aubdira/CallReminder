package com.example.aub.callreminder.broadcastreceivers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.util.Log;
import com.example.aub.callreminder.R;


/**
 * Created by aub on 12/28/17.
 * Project: CallReminder
 */

public class NotificationPublisher extends BroadcastReceiver {

    private static final String TAG = "NotificationPublisher";

    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String REASON = "reason";
    public static final int NOTIFICATION_ID = 1;

    @Override public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: broadcast started");
        if (intent != null) {
            String name = intent.getStringExtra(NAME);
            String phone = intent.getStringExtra(PHONE);
            String reason = intent.getStringExtra(REASON);
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = getNotification(context, name, phone, reason);
            if (manager != null) {
                manager.notify(NOTIFICATION_ID, notification);
            }
        }
    }

    private Notification getNotification(Context context, String contactName, String phoneNumber,
            String reason) {
        Resources resources = context.getResources();
        long[] pattern = {0, 300, 400, 300, 100, 0};

        Notification.Builder builder = new Notification.Builder(context);

        Notification.BigTextStyle bigTextStyle = new BigTextStyle();
        bigTextStyle.setBigContentTitle(resources.getString(R.string.app_name));
        bigTextStyle.bigText(resources.getString(R.string.notification_body, contactName, phoneNumber));
        bigTextStyle.setSummaryText(reason);

        builder.setContentTitle(resources.getString(R.string.app_name))
                .setContentText(resources.getString(R.string.notification_body, contactName, phoneNumber))
                .setSmallIcon(R.drawable.phone)
                .setStyle(bigTextStyle)
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        // notification cancel action
        Intent cancelIntent = new Intent();
        cancelIntent.setAction(NotificationReceiver.CANCEL_ACTION);
        PendingIntent cancelPending = PendingIntent
                .getBroadcast(context, 3, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.cancel_icon, resources.getString(R.string.notification_action_cancel),
                cancelPending);

        // notification call action
        Intent callIntent = new Intent();
        callIntent.putExtra("phone_number", phoneNumber);
        callIntent.setAction(NotificationReceiver.CALL_ACTION);
        PendingIntent callPending = PendingIntent
                .getBroadcast(context, 3, callIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.phone_call, resources.getString(R.string.notification_action_call),
                callPending);

        return builder.build();
    }

}
