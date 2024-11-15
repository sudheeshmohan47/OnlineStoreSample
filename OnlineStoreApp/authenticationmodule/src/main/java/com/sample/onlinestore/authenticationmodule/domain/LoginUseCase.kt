package com.sample.onlinestore.authenticationmodule.domain

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.authenticationmodule.data.model.login.LoginRequest
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferenceManagerRepository: PreferenceManagerRepository
) {
    suspend fun loginUser(
        username: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        loginRepository.loginUser(
            LoginRequest(
                username = username,
                password = password
            ),
            onComplete = { isSuccessful, domainResponse ->
                domainResponse.data?.let {
                    onComplete(isSuccessful)
                    saveSessionToken(it)
                }
            }
        )
    }

    private suspend fun saveSessionToken(sessionToken: String) {
        preferenceManagerRepository.saveUserSessionToken(sessionToken)
    }
}
