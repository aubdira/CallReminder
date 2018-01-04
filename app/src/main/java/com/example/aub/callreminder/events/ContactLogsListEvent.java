package com.example.aub.callreminder.events;

import com.example.aub.callreminder.database.Contact;
import java.util.List;


/**
 * Created by aub on 1/3/18.
 * Project: CallReminder
 */

public class ContactLogsListEvent {

    private List<Contact> contactLogsList;

    public List<Contact> getContactLogsList() {
        return contactLogsList;
    }

    public void setContactLogsList(List<Contact> contactLogsList) {
        this.contactLogsList = contactLogsList;
    }
}
