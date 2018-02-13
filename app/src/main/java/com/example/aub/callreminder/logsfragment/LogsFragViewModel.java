package com.example.aub.callreminder.logsfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aub on 1/30/18.
 * Project: CallReminder
 */
public class LogsFragViewModel extends ViewModel {

    @Inject
    ContactRepository contactRepository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LogsFragViewModel() {
        App.getContactRepositoryComponent().inject(this);
    }

    public LiveData<List<Contact>> getData() {
        return contactRepository.getContactsLogListByTimeDESC();
    }

    void deleteContact(Contact contact) {
        Disposable deleteContactDisposable = contactRepository.deleteContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                });

        compositeDisposable.add(deleteContactDisposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

}
