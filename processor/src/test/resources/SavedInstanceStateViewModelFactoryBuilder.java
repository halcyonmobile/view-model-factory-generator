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

public final class SavedInstanceStateViewModelFactoryBuilder {
    private final Context context;

    @Inject
    public SavedInstanceStateViewModelFactoryBuilder(Context context) {
        this.context = context;
    }

    public Factory build(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs) {
        return new Factory(owner, defaultArgs);
    }

    public Factory build(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs,
                         int x) {
        return new Factory(owner, defaultArgs, context, x);
    }

    public static final class Factory extends AbstractSavedStateViewModelFactory {
        int whichConstructor;

        private Context context;

        private int x_parameterOfConstructor1;

        private Factory(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs) {
            super(owner, defaultArgs);
            this.whichConstructor = 0;
        }

        private Factory(@NonNull SavedStateRegistryOwner owner, @Nullable Bundle defaultArgs,
                        Context context, int x) {
            super(owner, defaultArgs);
            this.whichConstructor = 1;
            this.context = context;
            this.x_parameterOfConstructor1 = x;
        }

        @NonNull
        @Override
        protected <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass,
                                                 @NonNull SavedStateHandle handle) {
            switch (whichConstructor) {
                case 0: return (T) new com.halcyonmobile.viewmodelfactory.sample.SavedInstanceStateViewModel(handle);
                case 1: return (T) new com.halcyonmobile.viewmodelfactory.sample.SavedInstanceStateViewModel(context, handle, x_parameterOfConstructor1);
                default: return null;
            }
        }
    }
}
