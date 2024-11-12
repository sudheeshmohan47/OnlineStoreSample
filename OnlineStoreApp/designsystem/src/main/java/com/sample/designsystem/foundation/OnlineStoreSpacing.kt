package com.sample.designsystem.foundation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sample.designsystem.foundation.OnlineStoreSpacing.LARGE
import com.sample.designsystem.foundation.OnlineStoreSpacing.MEDIUM
import com.sample.designsystem.foundation.OnlineStoreSpacing.MEDIUM_PLUS
import com.sample.designsystem.foundation.OnlineStoreSpacing.NONE
import com.sample.designsystem.foundation.OnlineStoreSpacing.SMALL
import com.sample.designsystem.foundation.OnlineStoreSpacing.EXTRA_LARGE
import com.sample.designsystem.foundation.OnlineStoreSpacing.EXTRA_SMALL

// Enum class representing different spacing options
enum class OnlineStoreSpacing {
    NONE,
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    MEDIUM_PLUS,
    LARGE,
    EXTRA_LARGE
}

/**
 * This function returns the Spacing in dp
 * Usage -> OnlineStoreSpacing.MEDIUM.dp()
 */
fun OnlineStoreSpacing.dp(): Dp =
    when (this) {
        NONE -> 0.dp
        EXTRA_SMALL -> 4.dp
        SMALL -> 8.dp
        MEDIUM -> 16.dp
        MEDIUM_PLUS -> 20.dp
        LARGE -> 24.dp
        EXTRA_LARGE -> 30.dp
    }
