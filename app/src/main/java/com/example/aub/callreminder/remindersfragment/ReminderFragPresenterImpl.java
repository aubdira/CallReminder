package com.example.aub.callreminder.remindersfragment;

/**
 * Created by aub on 12/25/17.
 * Project: CallReminder
 */

public class ReminderFragPresenterImpl implements ReminderFragPresenter  {

    private ReminderFragInteractor mInteractor;
    private ReminderFragView mReminderView;

    ReminderFragPresenterImpl(ReminderFragView reminderFragView) {
        mReminderView = reminderFragView;
        mInteractor = new ReminderFragInteractorImpl();
    }

    @Override public void loadData() {
        mInteractor.loadDataFromDatabase();
    }

    @Override public void onDetach() {
        mReminderView = null;
    }
}
