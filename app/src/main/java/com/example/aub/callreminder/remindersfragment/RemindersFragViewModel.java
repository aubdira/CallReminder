package com.example.aub.callreminder.remindersfragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by aub on 1/29/18.
 * Project: CallReminder
 */

public class RemindersFragViewModel extends ViewModel {
    
    @Inject ContactRepository contactRepository;
    
    public RemindersFragViewModel() {
        App.getContactRepositoryComponent().inject(this);
    }
    
    
    public LiveData<List<Contact>> getData() {
        return contactRepository.getContactsListByTimeASC();
    }
}
