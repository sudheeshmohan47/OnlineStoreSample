package com.sample.onlinestore.authenticationmodule.data

import com.sample.onlinestore.authenticationmodule.data.api.AuthenticationApiService
import com.sample.onlinestore.authenticationmodule.data.model.login.LoginRequest
import com.sample.onlinestore.authenticationmodule.domain.LoginRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import javax.inject.Inject

/**
 * Implementation of [LoginRepository] for handling user login.
 *
 * Responsibilities:
 * - Performs login via [AuthenticationApiService].
 * - Maps API errors and exceptions to domain-specific exceptions.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class LoginService @Inject constructor(
    private val authenticationApiService: AuthenticationApiService
) : LoginRepository {

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequest The login request object containing email/username and password.
     * @return [DomainResponse] containing the authentication token if successful.
     *
     * The function performs a network call to the authentication API:
     * - On success, returns a [DomainResponse] with the token.
     * - On failure, parses the error and throws a mapped exception.
     */
    override suspend fun loginUser(loginRequest: LoginRequest): DomainResponse<String> {
        try {
            val response = authenticationApiService.login(loginRequest)
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    return DomainResponse(data = loginResponse.token)
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }
}
