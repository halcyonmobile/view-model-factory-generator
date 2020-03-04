package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;

/**
 * A simple viewmodel with scope & more than one constructor.
 */
@Singleton
@ViewModelFactory
class MainViewModel extends ViewModel {

    MainViewModel(@Provided Context context) {
        System.err.println(context);
    }

    MainViewModel(@Provided Context context, int x) {

    }

}
