package com.example.aub.callreminder.logsfragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.ContactListEvent;
import com.example.aub.callreminder.events.ContactLogsListEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 1/3/18.
 * Project: CallReminder
 */

public class LogsFragInteractorImpl implements LogsFragInteractor {

    private ContactRepository mRepository;
    private List<Contact> contactsList;

    LogsFragInteractorImpl() {
        mRepository = new ContactRepository(App.getAppContext());
    }

    @SuppressLint("StaticFieldLeak") @Override public void loadDataFromDataBase() {
        new AsyncTask<Void, Void, List<Contact>>() {
            @Override protected List<Contact> doInBackground(Void... voids) {
                contactsList = mRepository.getContactsLogListByTimeASC();
                return contactsList;
            }

            @Override protected void onPostExecute(List<Contact> contacts) {
                ContactLogsListEvent event = new ContactLogsListEvent();
                event.setContactLogsList(contacts);
                EventBus.getDefault().post(event);
            }
        }.execute();
    }
}
