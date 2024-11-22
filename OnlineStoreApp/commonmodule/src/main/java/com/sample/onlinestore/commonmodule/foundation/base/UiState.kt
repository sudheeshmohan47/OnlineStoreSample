package com.sample.onlinestore.commonmodule.foundation.base

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.model.Message

interface State

/**
 * Every Screen should have these 3 states
 *
 */
sealed class UiState<T>(
    var data: T? = null,
    val error: DomainException? = null,
    val message: Message? = null
) : State {
    class Loading<T>(data: T?) : UiState<T>(data)
    class Result<T>(data: T?, message: Message? = null) :
        UiState<T>(data, message = message)
    class Error<T>(error: DomainException) : UiState<T>(error = error)
}
