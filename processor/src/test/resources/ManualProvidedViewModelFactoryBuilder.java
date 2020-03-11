package com.halcyonmobile.viewmodelfactory.sample;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ManualProvidedViewModelFactoryBuilder {
    private final int injected_int;

    private final StringBuilder stringBuilder;

    @Inject
    public ManualProvidedViewModelFactoryBuilder(int injected_int, StringBuilder stringBuilder) {
        this.injected_int = injected_int;
        this.stringBuilder = stringBuilder;
    }

    public Factory build(Context context, String alma) {
        return new Factory(context, alma);
    }

    public Factory build(Context context) {
        return new Factory(context, injected_int, stringBuilder);
    }

    public Factory build(String alma) {
        return new Factory(alma, injected_int, stringBuilder);
    }

    public static final class Factory implements ViewModelProvider.Factory {
        int whichConstructor;

        private Context context_parameterOfConstructor0;

        private String alma_parameterOfConstructor0;

        private Context context_parameterOfConstructor1;

        private int injected_int;

        private StringBuilder stringBuilder;

        private String alma_parameterOfConstructor2;

        private Factory(Context context, String alma) {
            this.whichConstructor = 0;
            this.context_parameterOfConstructor0 = context;
            this.alma_parameterOfConstructor0 = alma;
        }

        private Factory(Context context, int x, StringBuilder sb) {
            this.whichConstructor = 1;
            this.context_parameterOfConstructor1 = context;
            this.injected_int = x;
            this.stringBuilder = sb;
        }

        private Factory(String alma, int y, StringBuilder sb) {
            this.whichConstructor = 2;
            this.alma_parameterOfConstructor2 = alma;
            this.injected_int = y;
            this.stringBuilder = sb;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            switch (whichConstructor) {
                case 0: return (T) new com.halcyonmobile.viewmodelfactory.sample.ManualProvidedViewModel(context_parameterOfConstructor0, alma_parameterOfConstructor0);
                case 1: return (T) new com.halcyonmobile.viewmodelfactory.sample.ManualProvidedViewModel(context_parameterOfConstructor1, injected_int, stringBuilder);
                case 2: return (T) new com.halcyonmobile.viewmodelfactory.sample.ManualProvidedViewModel(alma_parameterOfConstructor2, injected_int, stringBuilder);
                default: return null;
            }
        }
    }
}
