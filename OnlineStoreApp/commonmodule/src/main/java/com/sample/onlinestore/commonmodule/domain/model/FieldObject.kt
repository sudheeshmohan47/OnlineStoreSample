package com.sample.onlinestore.commonmodule.domain.model

import androidx.annotation.StringRes

/**
 * Represents a field with a value and an optional error.
 */
data class FieldObject<T>(
    val value: T? = null,
    @StringRes val errorMessageResId: Int? = null,
    val errorMessage: String? = null
)
