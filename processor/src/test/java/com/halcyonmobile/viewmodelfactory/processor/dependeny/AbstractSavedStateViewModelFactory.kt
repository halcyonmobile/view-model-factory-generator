package androidx.lifecycle

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Test Double class for the AbstractSavedStateViewModelFactory of Android.
 *
 * It's needed so the generated test class can be compiled
 */
abstract class AbstractSavedStateViewModelFactory {

    constructor(@NonNull owner: SavedStateRegistryOwner, @Nullable defaultArgs: Bundle){

    }

    protected abstract fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T
}