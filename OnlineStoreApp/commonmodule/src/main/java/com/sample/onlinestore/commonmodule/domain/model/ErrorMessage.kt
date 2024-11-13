package com.sample.onlinestore.commonmodule.domain.model

import androidx.annotation.StringRes

data class ErrorMessage(
    @StringRes val messageResId: Int,
    val message: String = ""
)
