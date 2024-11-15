package com.sample.onlinestore.commonmodule.domain.exception

class RateLimitedException(override val message: String? = null) : DomainException()
