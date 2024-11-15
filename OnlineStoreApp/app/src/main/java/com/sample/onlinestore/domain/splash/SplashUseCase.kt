package com.sample.onlinestore.domain.splash

import com.sample.onlinestore.domain.splash.model.UserStatus
import javax.inject.Inject

class SplashUseCase @Inject constructor(private val splashRepository: SplashRepository) {
    suspend fun checkUserStatus(): UserStatus {
        val isAppIntroFinished = splashRepository.isAppIntroFinished()
        val sessionToken = splashRepository.getSessionToken()
        return UserStatus(
            sessionToken = sessionToken,
            isAppIntroFinished = isAppIntroFinished
        )
    }
}
