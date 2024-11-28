package com.sample.onlinestore.domain.splash

class FakeSplashRepository: SplashRepository {
    var appIntroFinished = false
    var sessionToken: String? = null

    override suspend fun isAppIntroFinished(): Boolean {
        return appIntroFinished
    }

    override suspend fun getSessionToken(): String? {
        return sessionToken
    }

    override suspend fun endUserSession() {
        // End user session
    }
}