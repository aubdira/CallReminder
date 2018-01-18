package com.example.aub.callreminder.remindersfragment;

import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.ContactListEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class ReminderFragInteractorImpl implements ReminderFragInteractor {

    @Inject ContactRepository mRepository;

    ReminderFragInteractorImpl() {
        App.getContactRepositoryComponent().inject(this);
    }

    @Override public void loadDataFromDatabase() {
        mRepository.getContactsListByTimeASC()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    ContactListEvent event = new ContactListEvent();
                    event.setContactList(contacts);
                    EventBus.getDefault().post(event);
                });
    }
}
