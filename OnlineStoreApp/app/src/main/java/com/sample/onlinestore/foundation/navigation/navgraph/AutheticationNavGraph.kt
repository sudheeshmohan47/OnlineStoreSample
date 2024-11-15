package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import com.carelo.android.presentation.authentication.login.screen.LoginScreen
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.slideComposable
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navigationmanagers.AuthenticationNavigationManager

fun NavGraphBuilder.authenticationNavGraph(
    appState: OnlineStoreAppState
) {
    val authenticationNavigationManager = AuthenticationNavigationManager(appState)
    loginScreenComposable(authenticationNavigationManager)
}

private fun NavGraphBuilder.loginScreenComposable(
    authenticationNavigationManager: AuthenticationNavigationManager
) {
    slideComposable<OnlineStoreScreens.Splash> {
        LoginScreen(
            gotoHomeScreen = {
                authenticationNavigationManager.goToDashboardScreen()
            }
        )
    }
}