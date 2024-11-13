package com.sample.onlinestore.commonmodule.domain.model

import com.sample.onlinestore.commonmodule.domain.exception.DomainException

/**
 * Wrapper object for response
 */
data class DomainResponse<T>(
    val data: T? = null,
    val error: DomainException? = null,
    val errorMessage: String? = null
)
