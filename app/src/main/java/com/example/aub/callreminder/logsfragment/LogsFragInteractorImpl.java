package com.example.aub.callreminder.logsfragment;

import android.util.Log;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.ContactRepository;
import com.example.aub.callreminder.events.ContactLogsListEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by aub on 1/3/18.
 * Project: CallReminder
 */

public class LogsFragInteractorImpl implements LogsFragInteractor {

    @Inject ContactRepository mRepository;

    private static final String TAG = "LogsFragInteractorImpl";

    LogsFragInteractorImpl() {
        App.getContactRepositoryComponent().inject(this);
    }

    @Override public void loadDataFromDataBase() {
        mRepository.getContactsLogListByTimeDESC()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    ContactLogsListEvent event = new ContactLogsListEvent();
                    event.setContactLogsList(contacts);
                    EventBus.getDefault().post(event);
                    Log.d(TAG, "loadDataFromDataBase: contacts size " + contacts.size());
                });
    }
}
