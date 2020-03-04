package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;

/**
 * A simple viewmodel with more than one constructor containing also savedState.
 */
@ViewModelFactory
class SavedInstanceStateViewModel extends ViewModel {

    SavedInstanceStateViewModel(@Provided Context context) {
        System.err.println(context);
    }

    SavedInstanceStateViewModel(@Provided Context context, SavedStateHandle savedStateHandle, int x) {

    }

    SavedInstanceStateViewModel(SavedStateHandle savedStateHandle) {

    }

}
