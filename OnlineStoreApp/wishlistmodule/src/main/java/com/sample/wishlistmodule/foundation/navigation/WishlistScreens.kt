package com.sample.wishlistmodule.foundation.navigation
import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

// Sealed class for different screens in the navigation
@kotlinx.serialization.Serializable
sealed class WishlistScreens : BaseScreen() {

    // App Initial Screens
    @Serializable
    data object WishlistScreen : WishlistScreens()
}
