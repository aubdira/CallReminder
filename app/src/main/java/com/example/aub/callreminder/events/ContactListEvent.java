package com.example.aub.callreminder.events;

import com.example.aub.callreminder.database.Contact;
import java.util.List;


/**
 * Created by aub on 12/26/17.
 * Project: CallReminder
 */

public class ContactListEvent {

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    private List<Contact> contactList;
}
