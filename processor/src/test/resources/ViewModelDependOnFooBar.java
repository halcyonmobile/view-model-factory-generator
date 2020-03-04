package com.halcyonmobile.viewmodelfactory.sample;

import com.halcyonmobile.viewmodelfactory.annotation.Provided;
import com.halcyonmobile.viewmodelfactory.annotation.ViewModelFactory;

import androidx.lifecycle.ViewModel;

/**
 * A viewModel which depends on an inner class of an other class [Foo.Bar]
 */
@ViewModelFactory
class ViewModelDependOnFooBar extends ViewModel {

    ViewModelDependOnFooBar(@Provided Foo.Bar fooBar) {
        System.err.println(fooBar);
    }

}
