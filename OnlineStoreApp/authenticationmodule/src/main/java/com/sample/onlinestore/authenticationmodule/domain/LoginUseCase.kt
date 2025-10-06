package com.sample.onlinestore.authenticationmodule.domain

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.authenticationmodule.data.model.login.LoginRequest
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferenceManagerRepository: PreferenceManagerRepository
) {
    suspend fun loginUser(username: String, password: String): Boolean {
        val domainResponse = loginRepository.loginUser(
            LoginRequest(
                username = username,
                password = password
            )
        )
        domainResponse.data?.let {
            saveSessionToken(it)
            return true
        }
        return false
    }

    private suspend fun saveSessionToken(sessionToken: String) {
        preferenceManagerRepository.saveUserSessionToken(sessionToken)
    }
}
