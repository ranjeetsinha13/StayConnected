package com.rs.stayconnected.di;

/**
 * Created by ranjeetsinha on 11/01/18.
 */

import android.arch.persistence.room.Room;

import com.rs.stayconnected.App;
import com.rs.stayconnected.db.ContactsDB;
import com.rs.stayconnected.repository.UserRepository;
import com.rs.stayconnected.repository.UserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;

@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    @Provides
    @Singleton
    UserRepository providesUserRepository(ContactsDB contactsDB) {
        return new UserRepositoryImpl(contactsDB);

    }

    @Provides
    @Singleton
    ContactsDB providesContactsDB(App app) {
        return Room.databaseBuilder(app.getApplicationContext(), ContactsDB.class, "users_db").build();
    }

}
