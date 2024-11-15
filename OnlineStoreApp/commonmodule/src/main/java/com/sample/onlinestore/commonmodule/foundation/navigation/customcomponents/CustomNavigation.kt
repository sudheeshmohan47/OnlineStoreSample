package com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen

// Extension property to check if the NavHostController's lifecycle is in a resumed state
val NavHostController.isLifecycleResumed: Boolean
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

/**
 * Navigates to the specified destination only if the NavHostController's lifecycle is in a resumed state.
 *
 * @param screen The screen to navigate to.
 */
fun NavHostController.navigateWithLifecycle(screen: BaseScreen) {
    if (this.isLifecycleResumed) {
        this.navigate(screen)
    }
}

/**
 * Pops the back stack and then navigates to the specified destination only if the NavHostController's
 * lifecycle is in a resumed state.
 *
 * @param screen The screen to navigate to after popping the back stack.
 */
fun NavHostController.navigateWithPopBackstack(screen: BaseScreen) {
    if (this.isLifecycleResumed) {
        this.popBackStack()
        this.navigate(screen) {
            launchSingleTop = true
        }
    }
}

/**
 * Pops the back stack only if the NavHostController's lifecycle is in a resumed state.
 */
fun NavHostController.popBackStackWithLifecycle() {
    // Check if the NavHostController's lifecycle is in a resumed state
    if (isLifecycleResumed) {
        this.popBackStack()
    }
}

/**
 * Navigates back to the previous destination with data by converting the value to JSON.
 * This function is useful for passing complex objects between destinations.
 *
 * @param key The key to identify the data.
 * @param value The value of the data to be passed.
 */
fun NavHostController.navigateBackWithData(
    key: String,
    value: Any
) {
    if (previousBackStackEntry?.id != null && isLifecycleResumed) {
        val jsonValue = Gson().toJson(value)
        previousBackStackEntry?.savedStateHandle?.set(key, jsonValue)
        popBackStack()
    }
}

/**
 * Extension function for [NavHostController] to navigate to a destination with a pop-up behavior.
 *
 * @param toScreen The destination screen where you want to navigate.
 * @param fromScreen The screen you want to pop up to, inclusive.
 */
fun NavHostController.navigateWithPopUpTo(
    toScreen: BaseScreen, // route name where you want to navigate
    fromScreen: BaseScreen // route you want from popUpTo.
) {
    if (isLifecycleResumed) {
        this.navigate(toScreen) {
            popUpTo(fromScreen) {
                inclusive = true
            }
        }
    }
}
