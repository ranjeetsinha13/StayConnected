package com.rs.stayconnected.di;

import com.rs.stayconnected.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class, ActivityModule.class
})
public interface AppComponent {

    void inject(App app);


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App app);

        AppComponent build();

    }
}