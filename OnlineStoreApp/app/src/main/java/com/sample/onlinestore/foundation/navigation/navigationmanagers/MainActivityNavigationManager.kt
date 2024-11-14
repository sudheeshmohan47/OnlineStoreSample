package com.sample.onlinestore.foundation.navigation.navigationmanagers

import androidx.navigation.NavHostController
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.appstate.popUp
import com.sample.onlinestore.foundation.appstate.switchBottomNavigationScreens

class MainActivityNavigationManager(private val appState: OnlineStoreAppState) {
    val switchScreens: (NavHostController, BaseScreen) -> Unit =
        { navController, screenToNavigate ->
            appState.switchBottomNavigationScreens(navController, screenToNavigate)
        }

    val backToPrevScreen: () -> Unit = {
        appState.popUp()
    }

    val endSessionAndGotoLogin: () -> Unit = {
        // appState.clearAndNavigate(OnlineStoreScreen.LoginScreen)
    }
}