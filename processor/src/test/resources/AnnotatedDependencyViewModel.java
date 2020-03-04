package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;
import com.halcyonmobile.viewmodelfactory.sample.di.ApplicationContext;

/**
 * A viewModel which uses [ApplicationContext] annotation on one of it's dependencies.
 */
@ViewModelFactory
class AnnotatedDependencyViewModel extends ViewModel {

    AnnotatedDependencyViewModel(@Provided @ApplicationContext Context context) {
        System.err.println(context);
    }

}
