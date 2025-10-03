@file:SuppressWarnings("RestrictedApi")
package com.sample.onlinestore.foundation.appstate

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen


/**
 * Pops the last destination off the back stack.
 */
fun OnlineStoreAppState.popUp() {
    backStack.removeLastOrNull()
}

/**
 * Pushes a new screen onto the back stack.
 */
fun OnlineStoreAppState.navigate(toScreen: BaseScreen) {
    backStack.add(toScreen)
}

/**
 * Replaces the current top screen with a new one.
 * If the stack is empty, adds the screen.
 */
fun OnlineStoreAppState.replace(toScreen: BaseScreen) {
    if (backStack.isNotEmpty()) {
        backStack.removeLastOrNull()
    }
    backStack.add(toScreen)
}

/**
 * Clears the back stack and navigates to the new screen.
 */
fun OnlineStoreAppState.clearAndNavigate(toScreen: BaseScreen) {
    backStack.clear()
    backStack.add(toScreen)
}

/**
 * Pops the back stack until the given screen.
 * @param inclusive If true, removes the target screen as well.
 */
fun OnlineStoreAppState.popUpTo(screenToPopTo: BaseScreen, inclusive: Boolean = true) {
    val index = backStack.indexOf(screenToPopTo)
    if (index != -1) {
        val endIndex = if (inclusive) index else index + 1
        for (i in backStack.size - 1 downTo endIndex) {
            backStack.removeAt(i)
        }
    }
}

/**
 * Switches bottom navigation screen by clearing stack and pushing new root.
 */
fun OnlineStoreAppState.switchBottomNavigationScreens(toScreen: BaseScreen) {
    backStack.clear()
    backStack.add(toScreen)
}

/**
 * Removes all instances of the given screen type from the back stack.
 */
fun OnlineStoreAppState.removeAllOfType(screenClass: Class<out BaseScreen>) {
    backStack.removeAll { screenClass.isInstance(it) }
}
/**
 * Pops the current screen and saves data for the previous screen.
 * @param key The key to associate with the data.
 * @param value The data to save for the previous screen.
 */
fun OnlineStoreAppState.popUpWithData(key: String, value: Any) {
    // Save data for the previous screen
    if (backStack.size > 1) { // Only if there is a previous screen
        backStackDataMap[key] = value
    }
    // Pop the current screen
    backStack.removeLastOrNull()
}

/**
 * Returns the current top screen in the back stack or null if empty.
 */
fun OnlineStoreAppState.currentScreen(): BaseScreen? = backStack.firstOrNull() as BaseScreen
