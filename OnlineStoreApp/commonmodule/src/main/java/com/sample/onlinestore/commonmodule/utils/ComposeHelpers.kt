package com.sample.onlinestore.commonmodule.utils

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner

/**
 * A helper for creating Hilt-injected [ViewModel]s that require assisted injection.
 *
 * This wraps the longer `hiltViewModel(viewModelStoreOwner, key, creationCallback)` call,
 * automatically resolving the current [ViewModelStoreOwner] from [LocalViewModelStoreOwner].
 *
 * @param VM  The type of the ViewModel
 * @param VMF The type of the assisted factory for the ViewModel
 * @param creationCallback A lambda that takes the factory and returns the ViewModel instance
 *
 * @return A [ViewModel] instance scoped to the current [ViewModelStoreOwner]
 */
@Composable
inline fun <reified VM : ViewModel, reified VMF> assistedHiltViewModel(
    noinline creationCallback: (VMF) -> VM
): VM {
    // Get the current ViewModelStoreOwner from the Compose environment
    val owner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    // Delegate to Hilt's hiltViewModel function with our provided creation callback
    return hiltViewModel(
        viewModelStoreOwner = owner,
        key = null,
        creationCallback = creationCallback
    )
}