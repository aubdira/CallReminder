package com.example.aub.callreminder.broadcastreceivers;

import android.Manifest.permission;
import android.annotation.SuppressLint;
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
    
    //    private static final String TAG = "NotificationReceiver";
    
    @SuppressLint("StaticFieldLeak")
    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        
        String action = intent.getAction();
        
        if (CALL_ACTION.equals(action)) {
            String phoneNumber = intent.getStringExtra("phone_number");
            
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
