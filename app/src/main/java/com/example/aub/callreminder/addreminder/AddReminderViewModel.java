package com.example.aub.callreminder.addreminder;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.NotifyMeEvent;
import com.example.aub.callreminder.utils.DateTimeConverter;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by aub on 1/30/18.
 * Project: CallReminder
 */
public class AddReminderViewModel extends AndroidViewModel {

  private static final String TAG = "AddReminderViewModel";

  @Inject ContactRepository mContactRepository;
  private Context mContext;

  private String mContactName;
  private String mContactPhoneNumber;
  private String mReminderReason;
  private String mTime = "Time";
  private String mDate = "Date";
  private int mHour;
  private int mMinute;
  private int mYear;
  private int mMonth;
  private int mDay;

  public AddReminderViewModel(@NonNull Application application) {
    super(application);
    App.getContactRepositoryComponent().inject(this);
    mContext = application;
  }

  String getContactName() {
    return mContactName;
  }

  void setContactName(String contactName) {
    mContactName = contactName;
  }

  String getContactPhoneNumber() {
    return mContactPhoneNumber;
  }

  void setContactPhoneNumber(String contactPhoneNumber) {
    mContactPhoneNumber = contactPhoneNumber;
  }

  String getReminderReason() {
    return mReminderReason;
  }

  void setReminderReason(String reminderReason) {
    mReminderReason = reminderReason;
  }

  public String getTime() {
    return mTime;
  }

  public void setTime(String time) {
    mTime = time;
  }

  String getDate() {
    return mDate;
  }

  void setDate(String date) {
    mDate = date;
  }

  int getHour() {
    return mHour;
  }

  void setHour(int hour) {
    mHour = hour;
  }

  int getMinute() {
    return mMinute;
  }

  void setMinute(int minute) {
    mMinute = minute;
  }

  int getYear() {
    return mYear;
  }

  void setYear(int year) {
    mYear = year;
  }

  int getMonth() {
    return mMonth;
  }

  void setMonth(int month) {
    mMonth = month;
  }

  int getDay() {
    return mDay;
  }

  void setDay(int day) {
    mDay = day;
  }

  long getTimeInMillis() {
    return DateTimeConverter.getTimeInMills(mYear, mMonth, mDay, mHour, mMinute);
  }

  /**
   * get contact information from the resolver
   *
   * see:https://stackoverflow.com/questions/32954413/android-contact-picker-get-name-number-email#32978754
   *
   * @param resolver contentResolver
   * @param data intent data
   */
  void getContactInfo(ContentResolver resolver, Intent data) {
    String contactName = "";
    String contactPhoneNumber = "";
    try {
      Uri uri = data.getData();

      // query uri for contact id and name
      Cursor cursor = null;
      if (uri != null) {
        cursor = resolver.query(uri, null, null, null, null);
      }
      if (cursor != null) {
        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
        contactName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));

        // query uri for contact phoneNumber
        Cursor phoneCursor =
            resolver.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = ?", new String[] { id },
                null);
        if (phoneCursor != null) {
          phoneCursor.moveToFirst();
          contactPhoneNumber =
              phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NORMALIZED_NUMBER));
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

    setContactName(contactName);
    setContactPhoneNumber(contactPhoneNumber);
  }

  void addContact() {
    Contact contact =
        new Contact(mContactName, mContactPhoneNumber, getTimeInMillis(), mReminderReason);
    mContactRepository.insertContact(contact)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new CompletableObserver() {
          @Override public void onSubscribe(Disposable d) {
          }

          @Override public void onComplete() {
            Log.d(TAG, "onComplete: successfully added Contact");
          }

          @Override public void onError(Throwable e) {
            Log.d(TAG, "onError: unable to add Contact");
          }
        });
  }

  /**
   * https://gist.github.com/BrandonSmith/6679223
   */
  void notifyMe() {
    Intent intent = new Intent(mContext, NotificationPublisher.class);
    intent.putExtra(NotificationPublisher.NAME, mContactName);
    intent.putExtra(NotificationPublisher.PHONE, mContactPhoneNumber);
    intent.putExtra(NotificationPublisher.REASON, mReminderReason);
    intent.putExtra(NotificationPublisher.TIME, getTimeInMillis());
    PendingIntent pendingIntent =
        PendingIntent.getBroadcast(mContext, (int) getTimeInMillis(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
      alarmManager.set(AlarmManager.RTC_WAKEUP, getTimeInMillis(), pendingIntent);
    }

    NotifyMeEvent event = new NotifyMeEvent();
    EventBus.getDefault().post(event);
  }
}
