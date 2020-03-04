package com.halcyonmobile.viewmodelfactory.sample

import android.content.Context
import androidx.lifecycle.ViewModel
import com.halcyonmobile.viewmodelfactory.annotation.Provided
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory
import javax.inject.Singleton

/**
 * A simple viewmodel in kotlin with scope & more than one constructor.
 */
@Singleton
@ViewModelFactory
internal class KotlinMainViewModel : ViewModel {

    constructor(@Provided context: Context) {
        System.err.println(context)
    }

    constructor(@Provided context: Context, x: Int) {

    }
}