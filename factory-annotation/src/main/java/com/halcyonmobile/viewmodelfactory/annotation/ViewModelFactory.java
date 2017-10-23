package com.halcyonmobile.viewmodelfactory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an {@link android.arch.lifecycle.ViewModel} class for which a
 * {@link android.arch.lifecycle.ViewModelProviders.Factory} builder will be generated.
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
