package com.sample.onlinestore.commonmodule.domain.exception

import com.sample.onlinestore.commonmodule.domain.exception.DomainException

class NotFoundException(override val message: String? = null) : DomainException()
