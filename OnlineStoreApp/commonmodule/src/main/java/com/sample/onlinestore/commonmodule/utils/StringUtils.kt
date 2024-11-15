package com.sample.onlinestore.commonmodule.utils

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.annotation.StringRes

/**
 * Retrieves a string from the provided string resource ID.
 *
 * @param context The Context used to access resources.
 * @param stringResourceId The resource ID of the string to retrieve.
 * @return The string associated with the resource ID, or an empty string if the resource is not found.
 *
 * Logs an error if the string resource is not found.
 */
fun getStringFromId(context: Context, @StringRes stringResourceId: Int): String {
    try {
        return context.getString(stringResourceId)
    } catch (e: NotFoundException) {
        Log.e(TagError, "String resource not found for ID: $stringResourceId")
    }
    // Return an empty string as a fallback
    return ""
}
