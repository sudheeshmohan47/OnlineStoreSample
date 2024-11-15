package com.sample.onlinestore.foundation.navigation.navigationmanagers

import com.sample.onlinestore.domain.splash.model.UserStatus
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState

class StartupNavigationManager(private val appState: OnlineStoreAppState) {

    val gotoLoginScreen: () -> Unit = {
        // appState.navigateWithPopBackstack(AuthenticationScreens.LoginScreen)
    }

    val goToAppIntroScreen: () -> Unit = {
        // appState.navigateWithPopBackstack(OnlineStoreScreens.AppIntroScreen)
    }

    val goToDashboardScreen: () -> Unit = {
        // appState.navigateWithPopBackstack(BottomNavItem.Home)
    }

    /**
     * Determines the screen to navigate based on the user's status and navigates accordingly.
     *
     * @param userStatus The status of the user, which includes login state and app intro completion.
     *
     * Navigation logic:
     * - If the app intro is not finished, navigates to the app intro screen.
     * - If the user is not logged in, navigates to the login screen.
     * - Otherwise, navigates to the dashboard screen.
     */
    fun navigateToScreens(
        userStatus: UserStatus?
    ) {
        userStatus?.let { status ->
            val isLoggedIn = !status.sessionToken.isNullOrEmpty()

            when {
                // Navigate to app intro if it's not finished
                !status.isAppIntroFinished -> goToAppIntroScreen()

                // Go to login screen if the user is not logged in
                !isLoggedIn -> gotoLoginScreen()

                // Default to dashboard screen
                else -> goToDashboardScreen()
            }
        }
    }
}
