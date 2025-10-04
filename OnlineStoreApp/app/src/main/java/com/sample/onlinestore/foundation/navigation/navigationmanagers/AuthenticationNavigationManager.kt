package com.sample.onlinestore.foundation.navigation.navigationmanagers

import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.appstate.clearAndNavigate
import com.sample.onlinestore.foundation.appstate.replace
import com.sample.onlinestore.presentation.dashboard.BottomNavItem

class AuthenticationNavigationManager(private val appState: OnlineStoreAppState) {
    val goToDashboardScreen: () -> Unit = {
        appState.replace(BottomNavItem.Home)
    }
}
