package com.sample.onlinestore.foundation.navigation.navigationmanagers

import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import timber.log.Timber

class AuthenticationNavigationManager(private val appState: OnlineStoreAppState) {
    val goToDashboardScreen: () -> Unit = {
        Timber.d("Login Success: goToDashboardScreen")
        // appState.navigateWithPopBackstack(BottomNavItem.Home)
    }
}