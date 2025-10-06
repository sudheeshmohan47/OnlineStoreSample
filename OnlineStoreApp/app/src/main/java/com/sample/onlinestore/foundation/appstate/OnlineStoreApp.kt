package com.sample.onlinestore.foundation.appstate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sample.onlinestore.authenticationmodule.foundation.navigation.AuthenticationScreens
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.foundation.base.getBaseRoute
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navgraph.rootUI
import com.sample.onlinestore.foundation.navigation.navigationmanagers.MainActivityNavigationManager
import com.sample.onlinestore.presentation.dashboard.BottomNavItem
import com.sample.onlinestore.presentation.dashboard.getScreenRoutesWithoutBottomNav
import com.sample.onlinestore.presentation.dashboard.screen.BottomNavigationSection
import com.sample.onlinestore.presentation.main.MainActivityAction
import com.sample.onlinestore.presentation.main.MainActivityUiModel

const val BottomNavAnimDuration = 1000

@Composable
fun OnlineStoreApp(
    uiState: UiState<MainActivityUiModel>,
    appState: OnlineStoreAppState,
    onAction: (MainActivityAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val currentRoute: String? = (appState.backStack.firstOrNull() as BaseScreen).getBaseRoute()

    // List of screens without bottom navigation
    val screensWithoutBottomNav = getScreenRoutesWithoutBottomNav()

    // We will hide bottom navigation for the screens without bottom navigation
    val showBottomBar = currentRoute != null && !screensWithoutBottomNav.contains(currentRoute)
    val mainActivityNavigationManager = MainActivityNavigationManager(appState)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }, // Slide in from the bottom
                    animationSpec = tween(durationMillis = BottomNavAnimDuration)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }, // Slide out to the bottom
                    animationSpec = tween(durationMillis = BottomNavAnimDuration)
                )
            ) {
                BottomNavigationSection(
                    mainUiState = uiState,
                    onAction = onAction,
                    switchToScreen = { screenToNavigate ->
                        mainActivityNavigationManager.switchScreens(
                            screenToNavigate
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavDisplay(
                entryDecorators = listOf(
                    // Add the default decorators for managing scenes and saving state
                    rememberSavedStateNavEntryDecorator(),
                    // Then add the view model store decorator
                    rememberViewModelStoreNavEntryDecorator()
                ),
                backStack = appState.backStack, onBack = { appState.popUp() },
                entryProvider = entryProvider {
                    rootUI(appState)
                }
            )
        }
    }
}

