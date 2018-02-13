package com.example.aub.callreminder.di.module;

import android.content.Context;

import com.example.aub.callreminder.database.ContactRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aub on 1/17/18.
 * Project: CallReminder
 */

@Module(includes = AppModule.class)
public class RepositoryContactModule {

    @Provides
    @Singleton
    public ContactRepository providesRepositoryModule(Context context) {
        return new ContactRepository(context);
    }
}
