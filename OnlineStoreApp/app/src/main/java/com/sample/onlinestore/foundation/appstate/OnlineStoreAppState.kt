package com.sample.onlinestore.foundation.appstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    backStack: NavBackStack<NavKey> = rememberNavBackStack(OnlineStoreScreens.Splash),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(
    backStack,
    coroutineScope
) {
    OnlineStoreAppState(backStack)
}

class OnlineStoreAppState(val backStack: NavBackStack<NavKey>){

    // Holds data associated with the previous screen
    val backStackDataMap = mutableMapOf<String, Any>()
    val backStackData: Map<String, Any> get() = backStackDataMap

    /**
     * Retrieves the value associated with the given [key] from the back stack data map
     * and removes it immediately. Use this to consume data passed via [popUpWithData]
     * so that it is only read once.
     *
     * @param key The key identifying the data to retrieve.
     * @return The value associated with the key, or null if no value exists.
     */
    fun getAndRemoveData(key: String): Any? = backStackDataMap.remove(key)
}
