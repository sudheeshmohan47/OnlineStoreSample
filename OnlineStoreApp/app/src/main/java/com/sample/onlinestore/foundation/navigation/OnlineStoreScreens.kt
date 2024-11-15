package com.sample.onlinestore.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

// Sealed class for different screens in the navigation
@Serializable
sealed class OnlineStoreScreens : BaseScreen() {

    // App Initial Screens
    @Serializable
    data object Splash : OnlineStoreScreens()
}
