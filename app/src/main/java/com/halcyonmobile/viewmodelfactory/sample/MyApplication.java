package com.halcyonmobile.viewmodelfactory.sample;

import com.halcyonmobile.viewmodelfactory.sample.di.AppModule;
import com.halcyonmobile.viewmodelfactory.sample.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
}
