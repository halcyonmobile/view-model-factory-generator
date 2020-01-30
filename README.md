# DEPRECATED: Square had the same idea, so I would kindly suggest to use that instead: https://github.com/square/AssistedInject #

This README would normally document whatever steps are necessary to get your application up and running.

### How do I get set up? ###
```groovy
kapt {
	correctErrorTypes = true
}

ext {
	viewModelGeneratorVersion = *latestVersionHere(0.0.1.beta1)*
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

### Who do I talk to? ###

* Gergely Hegedus 
* Arthur Nagy 
