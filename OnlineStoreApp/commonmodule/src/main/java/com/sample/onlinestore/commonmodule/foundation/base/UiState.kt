package com.sample.onlinestore.commonmodule.foundation.base

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.model.ErrorMessage

interface State

/**
 * Every Screen should have these 3 states
 *
 */
sealed class UiState<T>(
    var data: T? = null,
    val error: DomainException? = null,
    val errorMessage: ErrorMessage? = null
) : State {
    class Loading<T>(data: T?) : UiState<T>(data)
    class Result<T>(data: T?, errorMessage: ErrorMessage? = null) :
        UiState<T>(data, errorMessage = errorMessage)
    class Error<T>(error: DomainException) : UiState<T>(error = error)
}
