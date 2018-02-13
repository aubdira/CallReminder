package com.example.aub.callreminder.addreminder;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.aub.callreminder.database.ContactRepository;

import javax.inject.Inject;

/**
 * Created by aub on 2/9/18.
 * Project: CallReminder
 */
public class ReminderViewModelFactory implements ViewModelProvider.Factory {

    private ContactRepository contactRepository;

    @Inject
    public ReminderViewModelFactory(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddReminderViewModel.class)) {
            return (T) new AddReminderViewModel(contactRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
