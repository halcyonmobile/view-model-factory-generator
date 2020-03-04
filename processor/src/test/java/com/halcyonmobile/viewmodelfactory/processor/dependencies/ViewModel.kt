package androidx.lifecycle

/**
 * Test Double class for the ViewModel of Android.
 *
 * It's needed so the generated test class can be compiled
 */
abstract class ViewModel {
    protected open fun onCleared() {}
}