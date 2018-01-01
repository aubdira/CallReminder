package com.example.aub.callreminder.addreminder;

import android.content.ContentResolver;
import android.content.Intent;


/**
 * Created by aub on 12/20/17.
 * Project: CallReminder
 */

public interface AddReminderPresenter {

    void queryResolverForContactInfo(ContentResolver resolver, Intent data);

    void validateContactFields(String contactName, String phoneNumber, long timeInMills,
            String reminderReason);

    void onDetach();

    void createNotification(String contactName, String phoneNumber, String reason,
            long timeInMills);
}
