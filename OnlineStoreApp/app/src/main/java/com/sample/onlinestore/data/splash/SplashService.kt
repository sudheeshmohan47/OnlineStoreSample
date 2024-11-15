package com.sample.onlinestore.data.splash

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.domain.splash.SplashRepository
import javax.inject.Inject

class SplashService @Inject constructor(private val preferenceManagerRepository: PreferenceManagerRepository) :
    SplashRepository {

    override suspend fun isAppIntroFinished(): Boolean {
        return preferenceManagerRepository.isAppIntroFinished()
    }

    override suspend fun getSessionToken(): String {
        return preferenceManagerRepository.getSessionToken()
    }

    override suspend fun endUserSession() {
        preferenceManagerRepository.logoutUser()
    }
}
