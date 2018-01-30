package com.example.aub.callreminder.logsfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by aub on 1/30/18.
 * Project: CallReminder
 */

public class LogsFragViewModel extends ViewModel {
    
    private static final String TAG = "LogsFragViewModel";
    
    @Inject ContactRepository contactRepository;
    
    public LogsFragViewModel() {
        App.getContactRepositoryComponent().inject(this);
    }
    
    public LiveData<List<Contact>> getData() {
        return contactRepository.getContactsLogListByTimeDESC();
    }
    
    void deleteContact(Contact contact) {
        contactRepository.deleteContact(contact).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: contact deleted");
            }
            
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: unable to delete contact");
            }
        });
    }
}
