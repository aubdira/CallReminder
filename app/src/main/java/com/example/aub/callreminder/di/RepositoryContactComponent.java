package com.example.aub.callreminder.di;

import com.example.aub.callreminder.adapters.LogsFragAdapter;
import com.example.aub.callreminder.adapters.RemindersAdapter;
import com.example.aub.callreminder.addreminder.AddReminderInteractorImpl;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.di.module.RepositoryContactModule;
import com.example.aub.callreminder.logsfragment.LogsFragInteractorImpl;
import com.example.aub.callreminder.remindersfragment.RemindersFragViewModel;
import dagger.Component;
import javax.inject.Singleton;


/**
 * Created by aub on 1/17/18.
 * Project: CallReminder
 */

@Singleton
@Component(modules = {RepositoryContactModule.class})
public interface RepositoryContactComponent {

    void inject(AddReminderInteractorImpl interactor);

    void inject(LogsFragAdapter logsFragAdapter);

    void inject(RemindersAdapter remindersAdapter);

    void inject(LogsFragInteractorImpl logsFragInteractor);

    void inject(NotificationPublisher notificationPublisher);

    void inject(RemindersFragViewModel remindersFragViewModel);
}
