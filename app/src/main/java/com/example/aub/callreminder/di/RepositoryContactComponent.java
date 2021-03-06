package com.example.aub.callreminder.di;

import com.example.aub.callreminder.adapters.LogsFragAdapter;
import com.example.aub.callreminder.adapters.RemindersAdapter;
import com.example.aub.callreminder.addreminder.AddReminderViewModel;
import com.example.aub.callreminder.addreminder.ReminderViewModelFactory;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.di.module.RepositoryContactModule;
import com.example.aub.callreminder.logsfragment.LogsFragViewModel;
import com.example.aub.callreminder.remindersfragment.RemindersFragViewModel;
import com.example.aub.callreminder.utils.NotificationCreator;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aub on 1/17/18.
 * Project: CallReminder
 */

@Singleton
@Component(modules = {RepositoryContactModule.class})
public interface RepositoryContactComponent {

    void inject(LogsFragAdapter logsFragAdapter);

    void inject(RemindersAdapter remindersAdapter);

    void inject(NotificationPublisher notificationPublisher);

    void inject(RemindersFragViewModel remindersFragViewModel);

    void inject(LogsFragViewModel logsFragViewModel);

//    void inject(AddReminderViewModel addReminderViewModel);

    AddReminderViewModel addReminderViewModel();

    ReminderViewModelFactory reminderViewModelFactory();

    void inject(NotificationCreator notificationCreator);
}
