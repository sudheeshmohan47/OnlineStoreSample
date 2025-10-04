package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import com.sample.onlinestore.authenticationmodule.foundation.navigation.AuthenticationScreens
import com.sample.onlinestore.authenticationmodule.presentation.login.screen.LoginScreen
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigation.animatedEntry
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.navigationmanagers.AuthenticationNavigationManager

fun EntryProviderBuilder<NavKey>.authenticationNavGraph(
    appState: OnlineStoreAppState
) {
    val authenticationNavigationManager = AuthenticationNavigationManager(appState)
    loginScreenComposable(authenticationNavigationManager)
}

private fun EntryProviderBuilder<NavKey>.loginScreenComposable(
    authenticationNavigationManager: AuthenticationNavigationManager
) {
    animatedEntry<AuthenticationScreens.LoginScreen> {
        LoginScreen(
            gotoHomeScreen = {
                authenticationNavigationManager.goToDashboardScreen()
            }
        )
    }
}

