## Important note:

### Square had the same idea, but generalized, so I would kindly suggest to check it out if it fits your needs more: https://github.com/square/AssistedInject

### How do I get set up? ###
```groovy
kapt {
	correctErrorTypes = true
}

ext {
	viewModelGeneratorVersion = *latestVersionHere(1.0.1)*
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

        ViewModelProviders.of(this, factoryBuilder.build(getIntent().getIntExtra("MAIN_ACTIVITY_X", 0)).get(MainViewModel.class);
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
