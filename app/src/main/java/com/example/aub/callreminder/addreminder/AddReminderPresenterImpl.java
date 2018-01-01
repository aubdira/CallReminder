package com.example.aub.callreminder.addreminder;

import android.content.ContentResolver;
import android.content.Intent;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.utils.DateTimeConverter;


/**
 * Created by aub on 12/20/17.
 * Project: CallReminder
 */

public class AddReminderPresenterImpl implements AddReminderPresenter {

    private static final String TAG = "AddReminderPresenterImp";

    private AddReminderView mReminderView;
    private AddReminderInteractor mInteractor;

    AddReminderPresenterImpl(AddReminderView reminderView) {
        mReminderView = reminderView;
        mInteractor = new AddReminderInteractorImpl();
    }

    @Override public void queryResolverForContactInfo(ContentResolver resolver,
            Intent data) {
        mInteractor.getContactInfo(resolver, data);
    }

    @Override
    public void validateContactFields(String contactName, String phoneNumber, long timeInMills,
            String reminderReason) {
        if (phoneNumber.isEmpty()) {
            if (mReminderView != null) {
                mReminderView.phoneNumberRequiredError();
            }
            return;
        }
        if (DateTimeConverter.getNowDate() > timeInMills) {
            if (mReminderView != null) {
                mReminderView.displayFutureDateError();
            }
            return;
        }

        Contact contact = new Contact(contactName, phoneNumber, timeInMills, reminderReason);
        mInteractor.storeContact(contact);
    }

    @Override public void createNotification(String contactName, String phoneNumber, String reason,
            long timeInMills) {
        mInteractor.notifyMe(contactName, phoneNumber, reason, timeInMills);
    }

    @Override public void onDetach() {
        mReminderView = null;
    }
}
