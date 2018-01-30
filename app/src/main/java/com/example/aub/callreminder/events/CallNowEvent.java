package com.example.aub.callreminder.events;

/**
 * Created by aub on 1/2/18.
 * Project: CallReminder
 */

public class CallNowEvent {
    
    private String contactPhoneNumber;
    
    public CallNowEvent(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }
    
    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }
}
