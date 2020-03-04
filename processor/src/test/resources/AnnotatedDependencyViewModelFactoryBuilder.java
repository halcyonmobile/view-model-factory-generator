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
    public AnnotatedDependencyViewModelFactoryBuilder(@ApplicationContext Context context_ApplicationContext) {
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
