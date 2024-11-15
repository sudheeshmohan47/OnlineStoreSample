package com.sample.onlinestore.presentation.splash

import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event
import com.sample.onlinestore.domain.splash.model.UserStatus

data class SplashUiModel(
    val userStatus: UserStatus = getInitialUserStatus(),
    val initialDataLoaded: Boolean = false
)

sealed class SplashAction : Action {
    data class UpdateUserDetails(val userStatus: UserStatus) : SplashAction()
}

sealed class SplashEvent : Event

fun getInitialUserStatus(): UserStatus {
    return UserStatus(
        isAppIntroFinished = false,
        sessionToken = null
    )
}
