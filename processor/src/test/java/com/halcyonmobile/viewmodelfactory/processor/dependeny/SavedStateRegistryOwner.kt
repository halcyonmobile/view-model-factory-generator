package androidx.savedstate

/**
 * Test Double class for the SavedStateRegistryOwner of Android.
 *
 * It's needed so the generated test class can be compiled
 */
interface SavedStateRegistryOwner {
    val savedStateRegistry: SavedStateRegistry
}