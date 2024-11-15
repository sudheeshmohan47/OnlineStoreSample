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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.getBaseRoute
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navgraph.rootGraph
import com.sample.onlinestore.foundation.navigation.navigationmanagers.MainActivityNavigationManager
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
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.getBaseRoute()
    val screensWithoutBottomNav = getScreenRoutesWithoutBottomNav()
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
                            appState.navController,
                            screenToNavigate
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = appState.navController,
                startDestination = OnlineStoreScreens.Splash,
            ) {
                rootGraph(appState)
            }
        }
    }
}
