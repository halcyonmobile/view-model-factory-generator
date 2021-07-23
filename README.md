## Important note:

### Square had the same idea, but generalized, so I would kindly suggest to check it out if it fits your needs more: https://github.com/square/AssistedInject

### Ensure you have the HalcyonMobile GitHub Packages as a repository

```gradle
// top level build.gradle
{
allprojects {
    repositories {
        // ...
        maven {
            url "https://maven.pkg.github.com/halcyonmobile/android-common-extensions"
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

Note: you only need one maven declaration with "halcyonmobile/{specific}", every other package will be accessable.

### How do I get set up? ###

```groovy
kapt {
	correctErrorTypes = true
}

ext {
	viewModelGeneratorVersion = *latestVersionHere*
}
dependencies {
    implementation "com.halcyonmobile.viewmodelfactorygenerator:factory-annotation:$viewModelGeneratorVersion"
    kapt "com.halcyonmobile.viewmodelfactorygenerator:processor:$viewModelGeneratorVersion"
}
```
By default Kapt replaces every unknown type with NonExistentClass which can happen in this case when dagger's
annotation processing runs before the viewModelGenerator's. In order to change that behaviour you have to set
the correctErrorTypes flag.
[See more](https://kotlinlang.org/docs/reference/kapt.html#non-existent-type-correction)

### Usage

#### ViewModel

- Annotate your viewModel with the ViewModelFactory annotation
- Optionally add scope to the ViewModel, this will be used in the generated Factory
- Annotate any dependencies which come from dagger with @Provided.

Example:

```java
@Singleton
@ViewModelFactory
class MainViewModel extends ViewModel {

    MainViewModel(@Provided Context context, int x) {

    }

}
```

#### Activity / Fragment

- Inject the generated factory builder.
- Create the factory given the non-dagger dependencies

Example:

```java
public class MainActivity extends AppCompatActivity {

    @Inject
    MainViewModelFactoryBuilder factoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        ViewModelProvider(this, factoryBuilder.build(getIntent().getIntExtra("MAIN_ACTIVITY_X", 0)).get(MainViewModel.class);
    }
}
```

#### Provided via dagger / manual

- By default the parameters annotated with @Provided will be expected to come from dagger, this behaviour can be changed however.
- In ViewModelFactory annotation one can change the default meaning of @Provided by changing it's value to ViewModelFactory.ProvidedVia.MANUAL, in such case everything annotated with @Provided is expected to come from the generated build function while everything else expected from dagger

Example:
```java
@Singleton
@ViewModelFactory(ViewModelFactory.ProvidedVia.MANUAL)
class MainViewModel extends ViewModel {

    MainViewModel(@Provided Context context, int x) {

    }

}
```

Usage of generated class:

```java
public class MainActivity extends AppCompatActivity {

    @Inject
    MainViewModelFactoryBuilder factoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        ViewModelProvider(this, factoryBuilder.build(this).get(MainViewModel.class);
    }
}
```

##### Dagger 2.25.2 >
Some people had issues with the generated code after 2.25.2.
Couldn't figure out yet what's the cause, probably something changed in the Component generation. For now if this happens to what you can do is injecting Provider instead of the direct class, so it will be:
```java
public class MainActivity extends AppCompatActivity {

    @Inject
    Provider<MainViewModelFactoryBuilder> factoryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        ViewModelProvider(this, factoryBuilder.get().build(getIntent().getIntExtra("MAIN_ACTIVITY_X", 0)).get(MainViewModel.class);
    }
}
```

<h1 id="license">License :page_facing_up:</h1>

Copyright (c) 2020 Halcyon Mobile.
> https://www.halcyonmobile.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
