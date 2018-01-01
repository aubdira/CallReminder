package com.example.aub.callreminder.addreminder;

import android.content.ContentResolver;
import android.content.Intent;
import com.example.aub.callreminder.database.Contact;


/**
 * Created by aub on 12/20/17.
 * Project: CallReminder
 */

public interface AddReminderInteractor {

    void getContactInfo(ContentResolver resolver, Intent data);

    void storeContact(Contact contact);

    void notifyMe(String contactName, String phoneNumber, String reason, long timeInMills);
}
