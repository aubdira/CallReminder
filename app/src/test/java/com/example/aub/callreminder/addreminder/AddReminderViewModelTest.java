package com.example.aub.callreminder.addreminder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.aub.callreminder.database.ContactRepository;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.reactivex.Completable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aub on 2/7/18.
 * Project: CallReminder
 */
public class AddReminderViewModelTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Mock
    ContactRepository contactRepository;
    AddReminderViewModel addReminderViewModel;

    @BeforeClass
    public static void setUpClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(__ -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        addReminderViewModel = new AddReminderViewModel(contactRepository);
        addReminderViewModel.contactRepository = contactRepository;
    }

    @Test
    public void addContactSucceed() {
        when(contactRepository.insertContact(any())).thenReturn(Completable.complete());
        addReminderViewModel.addContact();
        verify(contactRepository).insertContact(any());
    }

    @AfterClass
    public static void afterClass() {
        RxAndroidPlugins.reset();
    }
}