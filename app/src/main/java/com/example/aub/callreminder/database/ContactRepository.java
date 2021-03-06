package com.example.aub.callreminder.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by aub on 12/22/17.
 * Project: CallReminder
 */

public class ContactRepository {

    private final ContactDao contactDao;

    public ContactRepository(Context context) {
        contactDao = DatabaseCreator.getAppDatabase(context).getContactDao();
    }

    //    public Maybe<Contact> getContactById(int id) {
    //        return contactDao.getContactById(id);
    //    }

    public LiveData<List<Contact>> getContactsListByTimeASC() {
        return contactDao.getContactsListByTimeASC();
    }

    public LiveData<List<Contact>> getContactsLogListByTimeDESC() {
        return contactDao.getContactsLogListByTimeDESC();
    }

    public Completable insertContact(Contact contact) {
        return Completable.fromAction(() -> contactDao.insertContact(contact));
    }

    public Completable deleteContact(Contact contact) {
        return Completable.fromAction(() -> contactDao.deleteContact(contact));
    }

    public Completable updateAsLog(long time) {
        return Completable.fromAction(() -> contactDao.updateContactAsLog(time));
    }
}
