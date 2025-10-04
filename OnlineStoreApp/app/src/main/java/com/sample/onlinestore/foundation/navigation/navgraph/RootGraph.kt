package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState

fun EntryProviderBuilder<NavKey>.rootUI(appState: OnlineStoreAppState) {
    splashAndAppIntroGraph(appState)
    authenticationNavGraph(appState)
    homeNavGraph(appState)
}
