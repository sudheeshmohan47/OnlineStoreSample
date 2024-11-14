@file:SuppressWarnings("RestrictedApi")
package com.sample.onlinestore.foundation.appstate

import androidx.navigation.NavHostController
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.isLifecycleResumed
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigateBackWithData
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigateWithPopBackstack
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.popBackStackWithLifecycle

/**
 * Pops the back stack if the lifecycle state of the current back stack entry associated
 * with the NavHostController is in a resumed state.
 */
fun OnlineStoreAppState.popUp() {
    navController.popBackStackWithLifecycle()
}

/**
 * Navigates to the specified screen and pops the back stack if the lifecycle state
 * of the current back stack entry associated with the NavHostController is in a resumed state.
 *
 * @param screen The destination screen to navigate to.
 */
fun OnlineStoreAppState.navigateWithPopBackstack(screen: BaseScreen) {
    navController.navigateWithPopBackstack(screen)
}

/**
 * Navigates to the specified screen if the lifecycle state of the current back stack entry
 * associated with the NavHostController is in a resumed state, and launch single top.
 *
 * @param screen The destination screen to navigate to.
 */
fun OnlineStoreAppState.navigate(screen: BaseScreen) {
    if (navController.isLifecycleResumed) {
        navController.navigate(screen) {
            launchSingleTop = true
        }
    }
}

/**
 * Navigates to the specified screen and pops the back stack up to the specified screen,
 * if the lifecycle state of the current back stack entry
 * associated with the NavHostController is in a resumed state.
 *
 * @param toScreen The destination screen to navigate to.
 * @param popUpToScreen The screen up to which the back stack should be popped.
 */
fun OnlineStoreAppState.navigateAndPopUpTo(toScreen: BaseScreen, popUpToScreen: BaseScreen) {
    if (navController.isLifecycleResumed) {
        navController.navigate(toScreen) {
            launchSingleTop = true
            popUpTo(popUpToScreen) { inclusive = true }
        }
    }
}

/**
 * Navigates to the specified screen with restored state, and pop up to the
 * specified screen, saving state, if the lifecycle state of the current back stack entry
 * associated with the NavHostController is in a resumed state.
 *
 * @param toScreen The destination screen to navigate to.
 * @param popUpToScreen The screen up to which the back stack should be popped.
 */
fun OnlineStoreAppState.navigateSaved(toScreen: BaseScreen, popUpToScreen: BaseScreen) {
    if (navController.isLifecycleResumed) {
        navController.navigate(toScreen) {
            launchSingleTop = true
            restoreState = true
            popUpTo(popUpToScreen) { saveState = true }
        }
    }
}

/**
 * Switches the current screen in the bottom navigation to the specified screen.
 * This function ensures that the new screen becomes the only screen in the
 * bottom navigation back stack.
 *
 * @param navController The NavHostController responsible for navigation.
 * @param toScreen The target screen to navigate to.
 */
fun OnlineStoreAppState.switchBottomNavigationScreens(
    navController: NavHostController,
    toScreen: BaseScreen,
) {
    navController.navigate(toScreen) {
        popUpTo(0) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * Navigates to the specified screen if the lifecycle state of the current back stack entry
 * associated with the NavHostController is in a resumed state and clears the entire back stack.
 *
 * @param screen The destination screen to navigate to.
 */
fun OnlineStoreAppState.clearAndNavigate(screen: BaseScreen) {
    if (navController.isLifecycleResumed) {
        navController.navigate(screen) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

/**
 * Pops the back stack up to the specified screen inclusively if the NavHostController's
 * lifecycle is in a resumed state.
 *
 * @param screenToPopupTo The screen up to which the back stack should be popped inclusively.
 */
fun OnlineStoreAppState.popupBackStackUpTo(screenToPopupTo: BaseScreen, inclusive: Boolean = true) {
    if (navController.isLifecycleResumed) {
        navController.popBackStack(screenToPopupTo, inclusive)
    }
}

/**
 * Pops the back stack of the associated NavHostController to the previous destination with data.
 * Internally uses Gson to convert Object to json
 *
 * @param key The key to identify the data.
 * @param value The value of the data to be passed.
 */
fun OnlineStoreAppState.popUpWithData(key: String, value: Any) {
    navController.navigateBackWithData(key, value)
}
