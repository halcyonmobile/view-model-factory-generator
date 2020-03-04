package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.halcyonmobile.viewmodelfactory.sample.di.ApplicationContext;
import java.lang.Class;
import java.lang.Override;
import javax.inject.Inject;

public final class AnnotatedDependencyViewModelFactoryBuilder {
    @ApplicationContext
    private final Context context_ApplicationContext;

    @Inject
    public AnnotatedDependencyViewModelFactoryBuilder(
            @ApplicationContext Context context_ApplicationContext) {
        this.context_ApplicationContext = context_ApplicationContext;
    }

    public Factory build() {
        return new Factory(context_ApplicationContext);
    }

    public static final class Factory implements ViewModelProvider.Factory {
        @ApplicationContext
        private Context context_ApplicationContext;

        private Factory(@ApplicationContext Context context) {
            this.context_ApplicationContext = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new com.halcyonmobile.viewmodelfactory.sample.AnnotatedDependencyViewModel(context_ApplicationContext);
        }
    }
}
