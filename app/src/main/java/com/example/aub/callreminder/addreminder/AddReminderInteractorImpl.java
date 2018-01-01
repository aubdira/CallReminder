package com.example.aub.callreminder.addreminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.ContactIdEvent;
import com.example.aub.callreminder.events.ContactNamePhoneEvent;
import com.example.aub.callreminder.events.NotifyMeEvent;
import com.example.aub.callreminder.events.StoreContactFinishedEvent;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 12/20/17.
 * Project: CallReminder
 */

public class AddReminderInteractorImpl implements AddReminderInteractor {

    private static final String TAG = "AddReminderInteractorIm";

    private ContactRepository mRepository;
    private Context mContext = App.getAppContext();

    AddReminderInteractorImpl() {
        mRepository = new ContactRepository(mContext);
    }

    /**
     * pick the name and phoneNumber from the data result see https://stackoverflow.com/questions/32954413/android-contact-picker-get-name-number-email#32978754
     *
     * @param data result from the contact picker dialog
     */
    @Override
    public void getContactInfo(ContentResolver resolver, Intent data) {
        String contactName = "";
        String contactPhoneNumber = "";
        try {
            Uri uri = data.getData();

            // query uri for contact id and name
            Cursor cursor = null;
            if (uri != null) {
                cursor = resolver.query(
                        uri, null, null, null, null);
            }
            if (cursor != null) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
                contactName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));

                // query uri for contact phoneNumber
                Cursor phoneCursor = resolver.query(
                        Phone.CONTENT_URI,
                        null,
                        Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );
                if (phoneCursor != null) {
                    phoneCursor.moveToFirst();
                    contactPhoneNumber = phoneCursor
                            .getString(phoneCursor.getColumnIndex(Phone.NORMALIZED_NUMBER));
                }

                if (phoneCursor != null) {
                    phoneCursor.close();
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContactNamePhoneEvent event = new ContactNamePhoneEvent(contactName, contactPhoneNumber);
        EventBus.getDefault().post(event);
    }

    @SuppressLint("StaticFieldLeak")
    @Override public void storeContact(final Contact contact) {

        new AsyncTask<Void, Void, Long>() {
            @Override protected Long doInBackground(Void... voids) {
                // insert the new contact
                return mRepository.insertContact(contact);
            }

            @Override protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                EventBus eventBus = EventBus.getDefault();

                ContactIdEvent event = new ContactIdEvent();
                event.setId(id);
                eventBus.post(event);

                StoreContactFinishedEvent finishedEvent = new StoreContactFinishedEvent();
                eventBus.post(finishedEvent);
            }
        }.execute();
    }

    /**
     * https://gist.github.com/BrandonSmith/6679223
     *
     * @param contactName name
     * @param phoneNumber phone
     * @param reason reason
     * @param timeInMills time
     */
    @Override
    public void notifyMe(String contactName, String phoneNumber, String reason, long timeInMills) {
        Intent intent = new Intent(mContext, NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NAME, contactName);
        intent.putExtra(NotificationPublisher.PHONE, phoneNumber);
        intent.putExtra(NotificationPublisher.REASON, reason);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext,
                (int) timeInMills,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMills, pendingIntent);
        }

        Log.d(TAG, "notifyMe: alarm set to " + timeInMills);
        NotifyMeEvent event = new NotifyMeEvent();
        EventBus.getDefault().post(event);
    }


}
