package com.rs.stayconnected.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.rs.stayconnected.viewmodel.UserDetailsViewModel;
import com.rs.stayconnected.viewmodel.UserListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel.class)
    abstract ViewModel bindUserListViewModel(UserListViewModel userListViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel.class)
    abstract ViewModel bindUserDetailsViewModel(UserDetailsViewModel userDetailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(UserViewModelFactory factory);

}
