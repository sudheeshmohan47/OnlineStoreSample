package com.sample.onlinestore.commonmodule.utils

/**
 * Extension function to extract the base route from a URL-like string by removing any query parameters.
 *
 * This function takes a string representing a route (potentially with query parameters),
 * and returns the base route by extracting the portion of the string before the "?" character.
 *
 * Example:
 * - Input: "com.carelo.android.Screen.Splash?deepLinkData=true"
 * - Output: "com.carelo.android.Screen.Splash"
 *
 * @receiver String The route string which may contain query parameters.
 * @return String The base route without query parameters.
 */
fun String.getBaseRoute(): String {
    return this.substringBefore("?")
}
