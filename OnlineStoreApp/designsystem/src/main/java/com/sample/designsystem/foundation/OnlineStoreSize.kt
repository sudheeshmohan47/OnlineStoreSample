package com.sample.designsystem.foundation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Enum class representing different sizing options with corresponding dp values
enum class OnlineStoreSize {
    NONE,
    EXTRA_SMALL,
    SMALL,
    MEDIUM,
    MEDIUM_PLUS,
    LARGE,
    XL,
    XXL,
    XXXL
}

/**
 * Extension function to convert OnlineStoreSize to dp
 *
 * Usage:
 * val mediumSize: Dp = OnlineStoreSize.MEDIUM.dp()
 *
 * @return the corresponding Dp value for the OnlineStoreSize
 */
fun OnlineStoreSize.dp(): Dp =
    when (this) {
        OnlineStoreSize.NONE -> 0.dp
        OnlineStoreSize.EXTRA_SMALL -> 36.dp
        OnlineStoreSize.SMALL -> 48.dp
        OnlineStoreSize.MEDIUM -> 56.dp
        OnlineStoreSize.MEDIUM_PLUS -> 64.dp
        OnlineStoreSize.LARGE -> 76.dp
        OnlineStoreSize.XL -> 84.dp
        OnlineStoreSize.XXL -> 96.dp
        OnlineStoreSize.XXXL -> 112.dp
    }

/**
 * Enum class representing different scale sizes used in the UI.
 *
 * Each constant corresponds to a specific scale factor, represented as a float value.
 * This is used to uniformly scale UI components to various sizes.
 *
 * @property value The scale factor as a float value.
 */
enum class OnlineStoreScaleSize(val value: Float) {
    QUARTER(0.25f),
    HALF(0.5f),
    THREE_QUARTERS(0.75f),
    ONE(1f),
    ONE_AND_A_QUARTER(1.25f),
    ONE_AND_A_HALF(1.5f),
    ONE_AND_THREE_QUARTERS(1.75f),
    TWO(2f),
    TWO_AND_A_QUARTER(2.25f),
    TWO_AND_A_HALF(2.5f)
}
