package com.example.aub.callreminder.database;

import android.content.Context;
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

    public List<Contact> getContactList() {
        return contactDao.getContactsList();
    }

    public Contact getContactById(int id) {
        return contactDao.getContactById(id);
    }

    public Contact getContactByTime(long time) {
        return contactDao.getContactByTime(time);
    }

    public List<Contact> getContactsListByTimeDESC() {
        return contactDao.getContactsListByTimeDESC();
    }

    public List<Contact> getContactsListByTimeASC() {
        return contactDao.getContactsListByTimeASC();
    }

    public long insertContact(Contact contact) {
        return contactDao.insertContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactDao.deleteContact(contact);
    }
}
