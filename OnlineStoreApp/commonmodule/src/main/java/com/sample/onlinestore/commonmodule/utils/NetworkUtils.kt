package com.sample.onlinestore.commonmodule.utils

import com.google.gson.Gson
import com.google.gson.JsonParseException
import retrofit2.Response

/**
 * Extension function to parse the error body of a Retrofit response using Gson.
 *
 * @param T The expected type of the error response body.
 * @return Parsed error response or null if parsing fails.
 */
inline fun <reified T> Response<*>?.parseErrorBody(): T? {
    return try {
        this?.errorBody()?.string()?.let {
            Gson().fromJson(it, T::class.java)
        }
    } catch (e: JsonParseException) {
        null
    }
}
