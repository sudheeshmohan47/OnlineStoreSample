package com.sample.onlinestore.authenticationmodule.domain

import com.sample.onlinestore.authenticationmodule.data.model.login.LoginRequest
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse

interface LoginRepository {
    suspend fun loginUser(loginRequest: LoginRequest): DomainResponse<String>
}
