package com.example.aub.callreminder.logsfragment;

/**
 * Created by aub on 1/3/18.
 * Project: CallReminder
 */

public class LogsFragPresenterImpl implements LogsFragPresenter {

    private LogsFragView mFragView;
    private LogsFragInteractor mInteractor;

    public LogsFragPresenterImpl(LogsFragView fragView) {
        mFragView = fragView;
        mInteractor = new LogsFragInteractorImpl();
    }

    @Override public void onDetach() {
        mFragView = null;
    }

    @Override public void loadData() {
        mInteractor.loadDataFromDataBase();
    }
}
