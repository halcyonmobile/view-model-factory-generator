package com.halcyonmobile.viewmodelfactory.sample

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.halcyonmobile.viewmodelfactory.annotation.Provided
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory

/**
 * A simple viewmodel with more than one constructor containing also savedState.
 */
@ViewModelFactory
internal class SavedInstanceStateViewModel : ViewModel {

    constructor(savedStateHandle: SavedStateHandle?) {}

    constructor(@Provided context: Context?, savedStateHandle: SavedStateHandle?, x: Int) {}
}