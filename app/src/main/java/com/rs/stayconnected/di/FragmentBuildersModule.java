package com.rs.stayconnected.di;

import com.rs.stayconnected.ui.details.ProfileFragment;
import com.rs.stayconnected.ui.list.ContactsListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract ContactsListFragment contributeContactsListFragment();
}