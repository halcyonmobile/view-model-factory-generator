package com.halcyonmobile.viewmodelfactory.sample;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.lang.Class;
import java.lang.Override;
import javax.inject.Inject;

public final class ViewModelDependOnFooBarFactoryBuilder {
    private final Foo.Bar fooBar;

    @Inject
    public ViewModelDependOnFooBarFactoryBuilder(Foo.Bar fooBar) {
        this.fooBar = fooBar;
    }

    public Factory build() {
        return new Factory(fooBar);
    }

    public static final class Factory implements ViewModelProvider.Factory {
        private Foo.Bar fooBar;

        private Factory(Foo.Bar fooBar) {
            this.fooBar = fooBar;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new com.halcyonmobile.viewmodelfactory.sample.ViewModelDependOnFooBar(fooBar);
        }
    }
}
