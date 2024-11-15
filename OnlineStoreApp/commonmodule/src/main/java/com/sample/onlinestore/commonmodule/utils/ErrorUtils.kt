package com.sample.onlinestore.commonmodule.utils

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.sample.onlinestore.commonmodule.domain.model.ErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.FieldObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun handleErrorMessage(
    context: Context,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    errorMessage: ErrorMessage,
    onSnackbarDisplayed: (() -> Unit)? = null
) {
    val message =
        errorMessage.message.ifEmpty {
            getStringFromId(
                context, errorMessage.messageResId
            )
        }
    showSnackBar(
        coroutineScope = coroutineScope,
        message = message,
        snackBarHostState = snackBarHostState,
        onSnackbarDisplayed = onSnackbarDisplayed
    )
}

/**
 * Retrieves the error message from the FieldObject.
 * If the errorMessage property of the FieldObject is not null or empty, it returns the errorMessage.
 * Otherwise, it retrieves the error message from a resource ID (errorMessageResId) using the getStringFromId function.
 * If errorMessageResId is null or 0, it returns an empty string.
 *
 * @param context The context used for retrieving the error message.
 * @return The error message, or an empty string if no error message is found.
 */
fun <T> FieldObject<T>.getErrorMessage(context: Context): String =
    if (!errorMessage.isNullOrEmpty()) {
        errorMessage
    } else getStringFromId(context, errorMessageResId ?: 0)

/**
 * Checks whether the FieldObject has an error.
 * An error is considered to be present if either:
 *  - The errorMessage property is not null or empty.
 *  - The errorMessageResId property is not null and greater than 0.
 *
 * @return true if the FieldObject has an error, false otherwise.
 */
fun <T> FieldObject<T>.hasError(): Boolean {
    val hasErrorMessage = !errorMessage.isNullOrEmpty()
    val hasErrorMessageResId = errorMessageResId != null && errorMessageResId > 0
    return hasErrorMessage || hasErrorMessageResId
}

// TODO Check unused
// Snackbar
// Method to show a snackbar when associated with user interaction
fun showSnackBar(
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String?,
    actionText: String? = null,
    onActionPerformed: (() -> Unit)? = null,
    onSnackbarDisplayed: (() -> Unit)? = null
) {
    coroutineScope.launch {
        message?.let { nonNullMessage ->
            val snackbarResult = snackBarHostState.showSnackbar(
                message = nonNullMessage,
                withDismissAction = true,
                actionLabel = actionText,
                duration = SnackbarDuration.Short,
            )
            onSnackbarDisplayed?.invoke()
            when (snackbarResult) {
                SnackbarResult.ActionPerformed, SnackbarResult.Dismissed -> {
                    onActionPerformed?.invoke()
                }
            }
        }
    }
}
