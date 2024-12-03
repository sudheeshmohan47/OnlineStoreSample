package com.sample.onlinestore.productsmodule.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class ProductScreens : BaseScreen() {

    @Serializable
    data class ProductDetailScreen(val productId: String) : ProductScreens()

    // Used for ui testing. Original products listing screen is under BottomNavItem.ProductListingScreen
    @Serializable
    data object ProductListingScreen : ProductScreens()
}
