package com.sample.onlinestore.commonmodule.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalView

private const val KeyboardDefaultHeightPercentage = 0.15
/**
 * A Composable function that observes the visibility state of the keyboard.
 * It uses `OnGlobalLayoutListener` to detect when the keyboard is shown or hidden,
 * by calculating the height of the visible display frame relative to the root view height.
 *
 * @return A [State] that holds a Boolean value indicating whether the keyboard is visible.
 */
@Composable
fun keyboardVisibility(): State<Boolean> {
    val keyboardVisibilityState = rememberSaveable { mutableStateOf(false) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardVisibilityState.value = keypadHeight > screenHeight * KeyboardDefaultHeightPercentage
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
    return keyboardVisibilityState
}
