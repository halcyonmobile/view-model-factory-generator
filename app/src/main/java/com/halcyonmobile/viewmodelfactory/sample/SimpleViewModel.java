package com.halcyonmobile.viewmodelfactory.sample;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;
import com.halcyonmobile.viewmodelfactory.sample.di.ApplicationContext;


@ViewModelFactory
class SimpleViewModel extends ViewModel {

    SimpleViewModel(@Provided @ApplicationContext Context context, @Nullable String intentExtra) {

    }
}
