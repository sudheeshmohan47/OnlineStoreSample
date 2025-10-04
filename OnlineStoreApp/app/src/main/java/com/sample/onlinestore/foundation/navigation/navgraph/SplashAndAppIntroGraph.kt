package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigation.animatedEntry
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navigationmanagers.StartupNavigationManager
import com.sample.onlinestore.presentation.appintro.appintro.screens.AppIntroScreen
import com.sample.onlinestore.presentation.splash.screen.SplashScreen

fun EntryProviderBuilder<NavKey>.splashAndAppIntroGraph(
    appState: OnlineStoreAppState
) {
    val startupNavigationManager = StartupNavigationManager(appState)

    // Splash Screen
    splashScreenComposable(startupNavigationManager)
    // App Intro Screen
    appIntroScreenComposable(startupNavigationManager)
}

private fun EntryProviderBuilder<NavKey>.splashScreenComposable(
    startupNavigationManager: StartupNavigationManager
) {
    animatedEntry<OnlineStoreScreens.Splash> {
        SplashScreen(
            navigateToScreens = { userStatus ->
                startupNavigationManager.navigateToScreens(
                    userStatus = userStatus
                )
            }
        )
    }
}

private fun EntryProviderBuilder<NavKey>.appIntroScreenComposable(
    startupNavigationManager: StartupNavigationManager
) {
    animatedEntry<OnlineStoreScreens.AppIntroScreen> {
        AppIntroScreen(goToLoginScreen = startupNavigationManager.gotoLoginScreen)
    }
}
