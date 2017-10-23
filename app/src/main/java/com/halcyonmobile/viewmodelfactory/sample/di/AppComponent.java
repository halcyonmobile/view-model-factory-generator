package com.halcyonmobile.viewmodelfactory.sample.di;


import com.halcyonmobile.viewmodelfactory.sample.MainActivity;
import com.halcyonmobile.viewmodelfactory.sample.MyApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilderModule.class})
public interface AppComponent extends AndroidInjector<MyApplication> {

    void inject(MainActivity mainActivity);
}
