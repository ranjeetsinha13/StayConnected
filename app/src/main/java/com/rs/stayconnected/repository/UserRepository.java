package com.rs.stayconnected.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rs.stayconnected.entity.User;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Completable;

@Singleton
public interface UserRepository {

    Completable addUser(User user);

    LiveData<List<User>> getUsersListByType(boolean type);

    Completable deleteUser(User user);

    LiveData<User> getUserDetails(String emailId);

    Completable updateUser(User user);

    Completable updateProfileInfo(@NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                                  String linkedinUrl, String twitterUrl, String photoURL);


}