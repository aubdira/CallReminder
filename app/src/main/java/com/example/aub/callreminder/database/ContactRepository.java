package com.example.aub.callreminder.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import io.reactivex.Flowable;
import java.util.List;


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

    public Flowable<List<Contact>> getContactsLogListByTimeDESC() {
        return contactDao.getContactsLogListByTimeDESC();
    }

    public long insertContact(Contact contact) {
        return contactDao.insertContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactDao.deleteContact(contact);
    }

    public int updateAsLog(long time) {
        return contactDao.updateContactAsLog(time);
    }
}
