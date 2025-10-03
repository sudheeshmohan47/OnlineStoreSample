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

class OnlineStoreAppState(val backStack: NavBackStack<NavKey>)
