package com.example.aub.callreminder;

import android.app.Application;
import com.example.aub.callreminder.di.DaggerRepositoryContactComponent;
import com.example.aub.callreminder.di.RepositoryContactComponent;
import com.example.aub.callreminder.di.module.AppModule;
import com.example.aub.callreminder.di.module.RepositoryContactModule;


/**
 * Created by aub on 12/23/17.
 * Project: CallReminder
 */

public class App extends Application {

    private static RepositoryContactComponent sRepositoryContactComponent;

    @Override public void onCreate() {
        super.onCreate();

        sRepositoryContactComponent = DaggerRepositoryContactComponent.builder()
                .appModule(new AppModule(this))
                .repositoryContactModule(new RepositoryContactModule())
                .build();
    }

    public static RepositoryContactComponent getContactRepositoryComponent() {
        return sRepositoryContactComponent;
    }
}
