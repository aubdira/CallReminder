package com.example.aub.callreminder.remindersfragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.ContactListEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class ReminderFragInteractorImpl implements ReminderFragInteractor {

    ContactRepository mRepository;
    List<Contact> contactsList;

    ReminderFragInteractorImpl() {
        mRepository = new ContactRepository(App.getAppContext());
    }

    @SuppressLint("StaticFieldLeak") @Override public void loadDataFromDatabase() {
        new AsyncTask<Void, Void, List<Contact>>() {
            @Override protected List<Contact> doInBackground(Void... voids) {
                contactsList = mRepository.getContactsListByTimeASC();
                return contactsList;
            }

            @Override protected void onPostExecute(List<Contact> contacts) {
                ContactListEvent event = new ContactListEvent();
                event.setContactList(contacts);
                EventBus.getDefault().post(event);
            }
        }.execute();
    }
}
