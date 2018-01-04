package com.example.aub.callreminder.broadcastreceivers;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.DeleteLogAdapterEvent;
import com.example.aub.callreminder.events.UpdateContactAsLogEvent;
import org.greenrobot.eventbus.EventBus;


/**
 * https://stackoverflow.com/questions/15350998/determine-addaction-click-for-android-notifications
 * Created by aub on 12/29/17.
 * Project: CallReminder
 */

public class NotificationReceiver extends BroadcastReceiver {

    public static final String CALL_ACTION = "call_action";
    public static final String CANCEL_ACTION = "cancel_action";

    private static final String TAG = "NotificationReceiver";

    @SuppressLint("StaticFieldLeak") @Override public void onReceive(final Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        String action = intent.getAction();
        final long timeInMillis = intent.getLongExtra("time_in_millis", 0);

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

//        // after canceling or accepting to call the number
//        // update the reminder as 'log'
//        new AsyncTask<Void, Void, Void>() {
//            @Override protected Void doInBackground(Void... voids) {
//                ContactRepository mRepository = new ContactRepository(context);
//                Contact contact = mRepository.getContactByTime(timeInMillis);
//                if (contact != null) {
//                    contact.setIsLog(1);
//                    mRepository.updateAsLog(contact);
//                } else {
//                    Log.d(TAG, "onHandleIntent: contact is null");
//                }
//                return null;
//            }
//
//            @Override protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                UpdateContactAsLogEvent event = new UpdateContactAsLogEvent();
//                EventBus.getDefault().post(event);
//            }
//        }.execute();
    }

}
