package com.sample.onlinestore.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

// Sealed class for startup screens
@Serializable
sealed class OnlineStoreScreens : BaseScreen() {

    @Serializable
    data object Splash : OnlineStoreScreens()

    @Serializable
    data object AppIntroScreen : OnlineStoreScreens()
}
