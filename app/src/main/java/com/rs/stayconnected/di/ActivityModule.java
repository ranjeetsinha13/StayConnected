package com.rs.stayconnected.di;

import com.rs.stayconnected.ui.details.ProfileFragment;
import com.rs.stayconnected.ui.list.ContactsListFragment;
import com.rs.stayconnected.ui.list.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract MainActivity contributeMainActivity();


}