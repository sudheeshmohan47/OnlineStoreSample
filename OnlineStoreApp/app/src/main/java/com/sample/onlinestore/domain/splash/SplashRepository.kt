package com.sample.onlinestore.domain.splash

interface SplashRepository {
    suspend fun isAppIntroFinished(): Boolean
    suspend fun getSessionToken(): String?
    suspend fun endUserSession()
}
