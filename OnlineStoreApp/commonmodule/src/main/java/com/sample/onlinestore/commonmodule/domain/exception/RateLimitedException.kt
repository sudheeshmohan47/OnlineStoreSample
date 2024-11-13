package com.sample.onlinestore.commonmodule.domain.exception

import com.sample.onlinestore.commonmodule.domain.exception.DomainException

class RateLimitedException(override val message: String? = null) : DomainException()