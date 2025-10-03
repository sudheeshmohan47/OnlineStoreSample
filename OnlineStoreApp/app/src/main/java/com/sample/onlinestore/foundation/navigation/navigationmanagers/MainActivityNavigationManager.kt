package com.sample.onlinestore.foundation.navigation.navigationmanagers

import androidx.navigation.NavHostController
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.sample.onlinestore.authenticationmodule.foundation.navigation.AuthenticationScreens
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.appstate.clearAndNavigate
import com.sample.onlinestore.foundation.appstate.popUp
import com.sample.onlinestore.foundation.appstate.switchBottomNavigationScreens
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens

class MainActivityNavigationManager(private val appState: OnlineStoreAppState) {
    val switchScreens: (BaseScreen) -> Unit =
        { screenToNavigate ->
            appState.switchBottomNavigationScreens(screenToNavigate)
        }

    val backToPrevScreen: () -> Unit = {
        appState.popUp()
    }

    val endSessionAndGotoLogin: () -> Unit = {
         appState.clearAndNavigate(AuthenticationScreens.LoginScreen)
    }
}
