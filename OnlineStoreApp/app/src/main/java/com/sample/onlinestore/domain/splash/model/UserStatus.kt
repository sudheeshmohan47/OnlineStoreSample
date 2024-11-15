package com.sample.onlinestore.domain.splash.model

data class UserStatus(
    val isAppIntroFinished: Boolean,
    val sessionToken: String?
)

