package com.rs.stayconnected.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.rs.stayconnected.dao.ContactsDao;
import com.rs.stayconnected.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class ContactsDB extends RoomDatabase {

    public abstract ContactsDao contactsDao();

}