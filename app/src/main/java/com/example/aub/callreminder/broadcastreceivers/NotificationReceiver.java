package com.example.aub.callreminder.broadcastreceivers;

import android.Manifest.permission;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;


/**
 * https://stackoverflow.com/questions/15350998/determine-addaction-click-for-android-notifications
 * Created by aub on 12/29/17.
 * Project: CallReminder
 */

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CALL_ACTION = "call_action";
    public static final String CANCEL_ACTION = "cancel_action";

    private static final String TAG = "NotificationReceiver";

    @Override public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        String action = intent.getAction();
        String phoneNumber = intent.getStringExtra("phone_number");

        if (CALL_ACTION.equals(action)) {

            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (ActivityCompat.checkSelfPermission(context, permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(callIntent);
                if (manager != null) {
                    manager.cancel(NotificationPublisher.NOTIFICATION_ID);
                }
            }

        } else if (CANCEL_ACTION.equals(action)) {
            if (manager != null) {
                manager.cancel(NotificationPublisher.NOTIFICATION_ID);
            }
        }
    }

}
