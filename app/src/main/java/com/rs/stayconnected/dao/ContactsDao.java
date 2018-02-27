package com.rs.stayconnected.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.rs.stayconnected.entity.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by ranjeetsinha on 10/01/18.
 */

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM " + User.TABLE_NAME + " WHERE userType =:userType")
    LiveData<List<User>> getUsersListByType(boolean userType);

    @Query("SELECT * FROM " + User.TABLE_NAME + " WHERE emailId = :emailId")
    LiveData<User> getUserDetails(@NonNull String emailId);

    @Insert(onConflict = REPLACE)
    void addUser(User user);

    @Delete
    void deleteUser(User user);

    @Update(onConflict = IGNORE)
    void updateUser(User user);

    @Query("UPDATE " + User.TABLE_NAME + " SET emailId=:emailId,name=:name,phoneNo=:phoneNo," +
            "facebookUrl=:facebookUrl,linkedinUrl=:linkedinUrl, twitterUrl=:twitterUrl," +
            "photoURL=:photoURL WHERE userType =:userType")
    void updateProfileInfo(String name, String emailId, String phoneNo, String facebookUrl, String linkedinUrl,
                           String twitterUrl, String photoURL, boolean userType);

}
