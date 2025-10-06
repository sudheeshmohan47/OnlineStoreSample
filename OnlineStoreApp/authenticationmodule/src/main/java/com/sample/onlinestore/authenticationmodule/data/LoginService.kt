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

@SuppressWarnings("TooGenericExceptionCaught")
class LoginService @Inject constructor(
    private val authenticationApiService: AuthenticationApiService
) : LoginRepository {

    /**
     * Attempts to log in the user with the provided login credentials.
     *
     * @param loginRequest The login request object containing user credentials.
     * @param onComplete A callback function that is invoked upon completion of the login process.
     *        - `Boolean`: Indicates success (true) or failure (false) of the login attempt.
     *        - `DomainResponse<String>`: Contains the login token if successful, or error details if failed.
     *
     * The function makes a network call to the authentication API to perform the login operation.
     * - If the login is successful, the callback is invoked with `true` and a `DomainResponse` containing the token.
     * - If unsuccessful, the error is parsed, mapped, and an exception is thrown, which can be handled by the caller.
     *
     * @throws Exception Mapped exceptions for API errors or any other failures encountered during login.
     */
    override suspend fun loginUser(loginRequest: LoginRequest) :DomainResponse<String> {
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
