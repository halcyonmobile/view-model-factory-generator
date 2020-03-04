package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.lang.Class;
import java.lang.Override;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class KotlinMainViewModelFactoryBuilder {
    @NotNull
    private final Context context;

    @Inject
    public KotlinMainViewModelFactoryBuilder(@NotNull Context context) {
        this.context = context;
    }

    public Factory build() {
        return new Factory(context);
    }

    public Factory build(int x) {
        return new Factory(context, x);
    }

    public static final class Factory implements ViewModelProvider.Factory {
        int whichConstructor;

        @NotNull
        private Context context;

        private int x_parameterOfConstructor1;

        private Factory(@NotNull Context context) {
            this.whichConstructor = 0;
            this.context = context;
        }

        private Factory(@NotNull Context context, int x) {
            this.whichConstructor = 1;
            this.context = context;
            this.x_parameterOfConstructor1 = x;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            switch (whichConstructor) {
                case 0: return (T) new com.halcyonmobile.viewmodelfactory.sample.KotlinMainViewModel(context);
                case 1: return (T) new com.halcyonmobile.viewmodelfactory.sample.KotlinMainViewModel(context, x_parameterOfConstructor1);
                default: return null;
            }
        }
    }
}
