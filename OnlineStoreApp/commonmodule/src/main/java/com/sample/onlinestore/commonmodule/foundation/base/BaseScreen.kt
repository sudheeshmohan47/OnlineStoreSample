package com.sample.onlinestore.commonmodule.foundation.base

import kotlinx.serialization.Serializable

// Base class for Screen to be used in app
@Serializable
open class BaseScreen

/**
 * Retrieves the base route string for a given BaseScreen.
 *
 * This function converts the class name to a route format by replacing
 * the dollar sign ('$') with a period ('.').
 *
 * Example: "com.onlinestore.android.Screen$DetailScreen" becomes "com.onlinestore.android.Screen.DetailScreen".
 *
 * @receiver BaseScreen The instance of BaseScreen.
 * @return String The formatted base route.
 */
fun BaseScreen.getBaseRoute(): String {
    // Replace '$' with '.' to get the correct route format
    return this::class.java.name.replace('$', '.')
}
