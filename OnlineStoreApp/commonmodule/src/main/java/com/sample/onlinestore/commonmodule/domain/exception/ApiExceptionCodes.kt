package com.sample.onlinestore.commonmodule.domain.exception

@SuppressWarnings("MagicNumber")
enum class ApiExceptionCodes(val errorCode: Int) {
    NOT_AVAILABLE(403),
    UNAUTHORIZED_ERROR(401),
    NOT_FOUND_ERROR(404),
    VALIDATION_FAILED(400),
}
