package com.sample.onlinestore.authenticationmodule.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
sealed class AuthenticationScreens : BaseScreen() {

    @Serializable
    data object LoginScreen : AuthenticationScreens()
}
