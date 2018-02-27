package com.rs.stayconnected.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.rs.stayconnected.entity.User;
import com.rs.stayconnected.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ranjeetsinha on 11/01/18.
 */

public class UserDetailsViewModel extends ViewModel {
    @Inject
    UserRepository userRepository;

    @Inject
    public UserDetailsViewModel() {

    }

    public LiveData<User> getUserDetails(String emailId) {
        return userRepository.getUserDetails(emailId);

    }

    public void addUser(@NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                        String linkedinUrl, String twitterUrl, boolean userType, String photoURL) {
        User user = new User(name, emailId, phoneNo, facebookUrl, linkedinUrl, twitterUrl, userType, photoURL);
        userRepository.addUser(user).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete - successfully added user");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Error occured when Adding user  %s", e.getMessage());

                    }
                });
    }


    public void updateUserProfileInfo(@NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                                      String linkedinUrl, String twitterUrl, String photoURL) {
        userRepository.updateProfileInfo(name, emailId, phoneNo, facebookUrl, linkedinUrl,
                twitterUrl, photoURL).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Timber.d("onComplete - successfully updated profile");

            }

            @Override
            public void onError(Throwable e) {
                Timber.d("Error occured when updating profile %s ", e.getMessage());
            }
        });

    }

    public void updateUser(User user, @NonNull String name, @NonNull String emailId, String phoneNo, String facebookUrl,
                           String linkedinUrl, String twitterUrl, boolean userType, String photoURL) {
        Timber.i("update user %s", user);
        if (user != null) {
            user.setEmailId(emailId);
            user.setFacebookUrl(facebookUrl);
            user.setLinkedinUrl(linkedinUrl);
            user.setName(name);
            user.setPhoneNo(phoneNo);
            user.setTwitterUrl(twitterUrl);
            user.setPhotoURL(photoURL);
            user.setUserType(false);


            userRepository.updateUser(user).observeOn(AndroidSchedulers.mainThread()).
                    subscribeOn(Schedulers.io()).
                    subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Timber.d("onComplete - successfully updated user");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.d("Error occured when updating user %s ", e.getMessage());

                        }
                    });

        } else {
            //Add user
            addUser(name, emailId, phoneNo, facebookUrl, linkedinUrl, twitterUrl, false, null);
            //throw new UserNotFoundException("The user does not exist");

        }


    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete - successfully deleted user");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("Error occured - in Deleting User %s", e.getMessage());

                    }
                });

    }


}
