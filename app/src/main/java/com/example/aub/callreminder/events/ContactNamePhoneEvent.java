package com.example.aub.callreminder.events;

/**
 * Created by aub on 12/27/17.
 * Project: CallReminder
 */

public class ContactNamePhoneEvent {

    private String name;
    private String phone;

    public ContactNamePhoneEvent(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
