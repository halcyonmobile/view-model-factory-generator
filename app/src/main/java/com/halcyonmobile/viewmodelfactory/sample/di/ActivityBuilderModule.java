package com.halcyonmobile.viewmodelfactory.sample.di;

import android.content.Context;

import com.halcyonmobile.viewmodelfactory.sample.MainActivity;

import dagger.*;
import dagger.android.ContributesAndroidInjector;
import kotlin.jvm.JvmStatic;

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector()
    abstract MainActivity contributeHomeActivity();

    @dagger.Module
    class HomeActivityModule {
        @ActivityContext
        @Provides
        @JvmStatic
        Context provideContext(MainActivity activity) {
            return activity;
        }
    }
}
