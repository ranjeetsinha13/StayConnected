package com.rs.stayconnected.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.rs.stayconnected.db.ContactsDB;
import com.rs.stayconnected.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * @author ranjeetsinha
 */
public class UserRepositoryImpl implements UserRepository {

    @Inject
    ContactsDB contactsDB;

    public UserRepositoryImpl(ContactsDB contactsDB) {
        this.contactsDB = contactsDB;
    }


    @Override
    public Completable addUser(User user) {
        return Completable.fromAction(() -> contactsDB.contactsDao().addUser(user));
    }

    @Override
    public LiveData<List<User>> getUsersListByType(boolean type) {
        return contactsDB.contactsDao().getUsersListByType(type);
    }

    @Override
    public Completable deleteUser(User user) {
        return Completable.fromAction(() -> contactsDB.contactsDao().deleteUser(user));
    }

    @Override
    public LiveData<User> getUserDetails(@NonNull String emailId) {
        return contactsDB.contactsDao().getUserDetails(emailId);
    }

    @Override
    public Completable updateUser(User user) {
        return Completable.fromAction(() -> contactsDB.contactsDao().updateUser(user));
    }

    @Override
    public Completable updateProfileInfo(@NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                                         String linkedinUrl, String twitterUrl, String photoURL) {
        return Completable.fromAction(() -> contactsDB.contactsDao().updateProfileInfo(name, emailId,
                phoneNo, facebookUrl, linkedinUrl, twitterUrl, photoURL, false));

    }
}
