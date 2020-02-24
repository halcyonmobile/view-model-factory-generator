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

package com.halcyonmobile.viewmodelfactory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an {@link androidx.lifecycle.ViewModel} class for which a
 * {@link androidx.lifecycle.ViewModelProviders.Factory} builder will be generated.
 * The generated class will have the name of the ViewModel postpended with FactoryBuilder and
 * will be in the same package.
 * For example, @{@link ViewModelFactory} MainViewModel will produce a FactoryBuilder named
 * MainViewModelFactoryBuilder.
 * <p>
 * The generated ViewModelFactoryBuilder will be part of daggers object-graph and can be injected.
 * To set a {@link javax.inject.Scope} to the FactoryBuilder you have to annotated your own
 * ViewModel with desired Scope.
 * <p>
 * For each constructor of the ViewModel a build method will be generated in the ViewModelFactory.
 * Every constructor parameter annotated with {@link Provided} will be provided via dagger and
 * every other constructor parameter has to be provided as a parameter in a build method.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ViewModelFactory {
}
