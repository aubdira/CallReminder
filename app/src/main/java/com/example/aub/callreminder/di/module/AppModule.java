package com.example.aub.callreminder.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aub on 1/17/18.
 * Project: CallReminder
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public Context providesApplication() {
        return mContext;
    }
}
