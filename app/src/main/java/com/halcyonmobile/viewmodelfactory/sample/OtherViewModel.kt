package com.halcyonmobile.viewmodelfactory.sample

import android.arch.lifecycle.ViewModel
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory

@ViewModelFactory
class OtherViewModel @JvmOverloads constructor(private val alma: Int?, private val piroska :String = "") : ViewModel()