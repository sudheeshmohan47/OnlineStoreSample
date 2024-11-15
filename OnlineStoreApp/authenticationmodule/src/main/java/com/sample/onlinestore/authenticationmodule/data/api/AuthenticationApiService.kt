package com.sample.onlinestore.authenticationmodule.data.api

import com.sample.onlinestore.authenticationmodule.data.model.login.LoginRequest
import com.sample.onlinestore.authenticationmodule.data.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApiService {

    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>
}
