package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.savedstate.SavedStateRegistryOwner;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import javax.inject.Inject;

public final class SavedStateWithManualProvidedViewModelFactoryBuilder {
    private final int injected_int;

    @Inject
    public SavedStateWithManualProvidedViewModelFactoryBuilder(int injected_int) {
        this.injected_int = injected_int;
    }

    public Factory build(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs) {
        return new Factory(owner, defaultArgs);
    }

    public Factory build(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs,
                         Context context) {
        return new Factory(owner, defaultArgs, context, injected_int);
    }

    public static final class Factory extends AbstractSavedStateViewModelFactory {
        int whichConstructor;

        private Context context_parameterOfConstructor1;

        private int injected_int;

        private Factory(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs) {
            super(owner, defaultArgs);
            this.whichConstructor = 0;
        }

        private Factory(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs,
                        Context context, int x) {
            super(owner, defaultArgs);
            this.whichConstructor = 1;
            this.context_parameterOfConstructor1 = context;
            this.injected_int = x;
        }

        @NonNull
        @Override
        protected <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass,
                                                 @NonNull SavedStateHandle handle) {
            switch (whichConstructor) {
                case 0: return (T) new com.halcyonmobile.viewmodelfactory.sample.SavedStateWithManualProvidedViewModel(handle);
                case 1: return (T) new com.halcyonmobile.viewmodelfactory.sample.SavedStateWithManualProvidedViewModel(context_parameterOfConstructor1, handle, injected_int);
                default: return null;
            }
        }
    }
}
