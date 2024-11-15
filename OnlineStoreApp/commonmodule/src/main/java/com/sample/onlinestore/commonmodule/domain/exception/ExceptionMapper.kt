package com.sample.onlinestore.commonmodule.domain.exception

import com.sample.onlinestore.commonmodule.R
import com.sample.onlinestore.commonmodule.domain.model.ErrorMessage
import java.net.UnknownHostException

fun mapException(e: Throwable): Throwable =
    when (e) {
        is UnknownHostException -> NetworkException()
        else -> GenericException()
    }

fun mapErrors(code: Int, message: String? = ""): DomainException =
    when (code) {
        ApiExceptionCodes.NOT_AVAILABLE.errorCode -> ServerNotAvailableException(message)
        ApiExceptionCodes.UNAUTHORIZED_ERROR.errorCode -> UnauthorizedException(message)
        ApiExceptionCodes.NOT_FOUND_ERROR.errorCode -> NotFoundException(message)
        ApiExceptionCodes.VALIDATION_FAILED.errorCode -> ServerValidationException(message)
        else -> GenericException(message)
    }

fun mapErrorMessage(e: DomainException): ErrorMessage {
    return when (e) {
        is ServerNotAvailableException -> ErrorMessage(R.string.error_server_not_available)
        is NetworkException -> ErrorMessage(R.string.error_no_internet)
        else -> ErrorMessage(R.string.error_something_went_wrong)
    }
}
