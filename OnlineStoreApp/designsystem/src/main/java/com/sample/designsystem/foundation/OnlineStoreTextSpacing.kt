package com.sample.designsystem.foundation

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

enum class OnlineStoreTextSpacing {
    NONE,
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    MEDIUM_PLUS,
    LARGE,
    EXTRA_LARGE
}

/**
 * This function returns the Text Spacing in sp
 * Usage -> OnlineStoreSpacing.MEDIUM.sp()
 */
fun OnlineStoreTextSpacing.sp(): TextUnit =
    when (this) {
        OnlineStoreTextSpacing.NONE -> 0.sp
        OnlineStoreTextSpacing.EXTRA_SMALL -> 0.10.sp
        OnlineStoreTextSpacing.SMALL -> 0.20.sp
        OnlineStoreTextSpacing.MEDIUM -> 0.30.sp
        OnlineStoreTextSpacing.MEDIUM_PLUS -> 0.40.sp
        OnlineStoreTextSpacing.LARGE -> 0.50.sp
        OnlineStoreTextSpacing.EXTRA_LARGE -> 0.60.sp
    }
