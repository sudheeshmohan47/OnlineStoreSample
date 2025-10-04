package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import com.sample.onlinestore.cartmodule.foundation.navigation.CartScreens
import com.sample.onlinestore.cartmodule.presentation.cart.screen.CartScreen
import com.sample.onlinestore.categoriesmodule.presentation.categories.screen.CategoriesScreen
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.navigation.animatedEntry
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.slideComposable
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.navigationmanagers.HomeNavigationManager
import com.sample.onlinestore.presentation.dashboard.BottomNavItem
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens
import com.sample.onlinestore.productsmodule.presentation.productdetails.screen.ProductDetailsScreen
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductsListingScreen
import com.sample.wishlistmodule.presentation.wishlist.screen.WishlistScreen

fun EntryProviderBuilder<NavKey>.homeNavGraph(
    appState: OnlineStoreAppState
) {
    val homeNavigationManager = HomeNavigationManager(appState)
    // HomeScreen
    homeScreenComposable(homeNavigationManager)
    // Product Details Screen
    productDetailsScreenComposable(homeNavigationManager)
    // Categories Screen
    categoriesScreenComposable()
    // Cart Screen
    cartScreenComposable(homeNavigationManager)
    // Wishlist Screen
    wishlistScreenComposable(homeNavigationManager)
}

private fun EntryProviderBuilder<NavKey>.homeScreenComposable(
    homeNavigationManager: HomeNavigationManager
) {
    entry<BottomNavItem.Home> {
        ProductsListingScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen
        )
    }
}

private fun EntryProviderBuilder<NavKey>.productDetailsScreenComposable(homeNavigationManager: HomeNavigationManager) {
    animatedEntry<ProductScreens.ProductDetailScreen> { productDetailsScreen ->

        ProductDetailsScreen(
            backToPreviousScreen = homeNavigationManager.backToPreviousScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen,
            productId = productDetailsScreen.productId
        )
    }
}

private fun EntryProviderBuilder<NavKey>.categoriesScreenComposable() {
    entry<BottomNavItem.Categories> {
        CategoriesScreen()
    }
}

private fun EntryProviderBuilder<NavKey>.cartScreenComposable(homeNavigationManager: HomeNavigationManager) {
    entry<CartScreens.CartScreen> {
        CartScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            backToPreviousScreen = homeNavigationManager.backToPreviousScreen
        )
    }
}

private fun EntryProviderBuilder<NavKey>.wishlistScreenComposable(homeNavigationManager: HomeNavigationManager) {
    entry<BottomNavItem.Wishlist> {
        WishlistScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen
        )
    }
}
