package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.slideComposable
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navigationmanagers.StartupNavigationManager
import com.sample.onlinestore.presentation.appintro.appintro.screens.AppIntroScreen
import com.sample.onlinestore.presentation.splash.screen.SplashScreen

fun NavGraphBuilder.splashAndAppIntroGraph(
    appState: OnlineStoreAppState
) {
    val startupNavigationManager = StartupNavigationManager(appState)

    // Splash Screen
    splashScreenComposable(startupNavigationManager)
    // App Intro Screen
    appIntroScreenComposable(startupNavigationManager)
}

private fun NavGraphBuilder.splashScreenComposable(
    startupNavigationManager: StartupNavigationManager
) {
    slideComposable<OnlineStoreScreens.Splash> {
        SplashScreen(
            navigateToScreens = { userStatus ->
                startupNavigationManager.navigateToScreens(
                    userStatus = userStatus
                )
            }
        )
    }
}

private fun NavGraphBuilder.appIntroScreenComposable(
    startupNavigationManager: StartupNavigationManager
) {
    slideComposable<OnlineStoreScreens.AppIntroScreen> {
        AppIntroScreen(goToLoginScreen = startupNavigationManager.gotoLoginScreen)
    }
}
