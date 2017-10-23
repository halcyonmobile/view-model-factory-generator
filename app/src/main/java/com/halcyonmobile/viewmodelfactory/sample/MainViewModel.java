package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;
import com.halcyonmobile.viewmodelfactory.sample.di.ApplicationContext;

import javax.inject.Singleton;

@Singleton
@ViewModelFactory
class MainViewModel extends android.arch.lifecycle.ViewModel {

    MainViewModel(@Provided @ApplicationContext Context context) {
        System.err.println(context);
    }

    MainViewModel(@Provided @ApplicationContext Context context, int x) {

    }

}
