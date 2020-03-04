package androidx.lifecycle

/**
 * Test Double class for the ViewModelProvider of Android.
 *
 * It's needed so the generated test class can be compiled
 */

class ViewModelProvider {

    interface Factory {
        fun <T : ViewModel> create(modelClass: Class<T>): T
    }
}