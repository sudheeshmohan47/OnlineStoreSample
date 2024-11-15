package com.sample.onlinestore.presentation.appintro.appintro

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AppIntroPage(
    @DrawableRes
    val imageId: Int,
    @StringRes
    val titleResId: Int,
    @StringRes
    val subTitleResId: Int
)
