package com.rs.stayconnected;

import android.app.Activity;
import android.app.Application;

import com.facebook.FacebookSdk;
import com.rs.stayconnected.di.AppInjector;
import com.twitter.sdk.android.core.Twitter;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;


public class App extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
        FacebookSdk.sdkInitialize(this);
        Twitter.initialize(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}