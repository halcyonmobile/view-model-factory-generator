/*
 * Copyright (c) 2020 Halcyon Mobile.
 * https://www.halcyonmobile.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
