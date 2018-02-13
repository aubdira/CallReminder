package com.example.aub.callreminder.addreminder;

import android.content.Context;

import com.example.aub.callreminder.database.ContactRepository;

/**
 * Created by aub on 2/9/18.
 * Project: CallReminder
 */
class Injection {

    public static ReminderViewModelFactory provideViewModelFactory(Context context) {
        ContactRepository contactRepository = new ContactRepository(context);
        return new ReminderViewModelFactory(contactRepository);
    }
}
