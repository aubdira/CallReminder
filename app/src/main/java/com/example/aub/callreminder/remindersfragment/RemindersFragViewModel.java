package com.example.aub.callreminder.remindersfragment;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.example.aub.callreminder.App;
import com.example.aub.callreminder.broadcastreceivers.NotificationPublisher;
import com.example.aub.callreminder.database.Contact;
import com.example.aub.callreminder.database.ContactRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by aub on 1/29/18.
 * Project: CallReminder
 */

public class RemindersFragViewModel extends AndroidViewModel {

  @Inject ContactRepository contactRepository;
  private Context context;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  public RemindersFragViewModel(@NonNull Application application) {
    super(application);
    App.getContactRepositoryComponent().inject(this);
    context = application;
  }

  public LiveData<List<Contact>> getData() {
    return contactRepository.getContactsListByTimeASC();
  }

  void cancelReminder(Contact contact) {

    Disposable deleteContactDisposable = contactRepository.deleteContact(contact)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {
          // cancel the alarm manager
          Intent intent = new Intent(context, NotificationPublisher.class);
          PendingIntent pendingIntent =
              PendingIntent.getBroadcast(context, (int) contact.getReminderTime(), intent,
                  PendingIntent.FLAG_UPDATE_CURRENT);

          AlarmManager alarmManager =
              (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
          if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
          }
        });

    compositeDisposable.add(deleteContactDisposable);
  }

  void clear() {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.clear();
    }
    context = null;
  }
}
