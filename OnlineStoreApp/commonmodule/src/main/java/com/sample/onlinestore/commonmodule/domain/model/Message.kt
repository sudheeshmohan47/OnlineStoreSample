package com.sample.onlinestore.commonmodule.domain.model

import androidx.annotation.StringRes

data class Message(
    @StringRes val messageResId: Int,
    val message: String = "",
    val currentTimeInMillis: Long = System.currentTimeMillis()
)
