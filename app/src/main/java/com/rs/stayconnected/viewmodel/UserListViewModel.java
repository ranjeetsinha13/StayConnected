package com.rs.stayconnected.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rs.stayconnected.entity.User;
import com.rs.stayconnected.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ranjeetsinha on 11/01/18.
 */

public class UserListViewModel extends ViewModel {


    @Inject
    UserRepository userRepository;

    @Inject
    public UserListViewModel() {

    }

    public LiveData<List<User>> getUsersListByType(boolean userType) {
        return userRepository.getUsersListByType(userType);
    }


}
