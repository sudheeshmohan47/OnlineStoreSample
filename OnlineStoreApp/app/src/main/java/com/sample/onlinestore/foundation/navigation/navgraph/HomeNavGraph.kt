package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sample.onlinestore.cartmodule.foundation.navigation.CartScreens
import com.sample.onlinestore.cartmodule.presentation.cart.screen.CartScreen
import com.sample.onlinestore.categoriesmodule.presentation.categories.screen.CategoriesScreen
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.slideComposable
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.navigationmanagers.HomeNavigationManager
import com.sample.onlinestore.presentation.dashboard.BottomNavItem
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens
import com.sample.onlinestore.productsmodule.presentation.productdetails.screen.ProductDetailsScreen
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductsListingScreen
import com.sample.wishlistmodule.presentation.wishlist.screen.WishlistScreen

fun NavGraphBuilder.homeNavGraph(
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

private fun NavGraphBuilder.homeScreenComposable(
    homeNavigationManager: HomeNavigationManager
) {
    composable<BottomNavItem.Home> {
        ProductsListingScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen
        )
    }
}

private fun NavGraphBuilder.productDetailsScreenComposable(homeNavigationManager: HomeNavigationManager) {
    slideComposable<ProductScreens.ProductDetailScreen> {

        val productDetailScreen = it.toRoute<ProductScreens.ProductDetailScreen>()
        ProductDetailsScreen(
            backToPreviousScreen = homeNavigationManager.backToPreviousScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen,
            productId = productDetailScreen.productId
        )
    }
}

private fun NavGraphBuilder.categoriesScreenComposable() {
    composable<BottomNavItem.Categories> {
        CategoriesScreen()
    }
}

private fun NavGraphBuilder.cartScreenComposable(homeNavigationManager: HomeNavigationManager) {
    composable<CartScreens.CartScreen> {
        CartScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            backToPreviousScreen = homeNavigationManager.backToPreviousScreen
        )
    }
}

private fun NavGraphBuilder.wishlistScreenComposable(homeNavigationManager: HomeNavigationManager) {
    composable<BottomNavItem.Wishlist> {
        WishlistScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen,
            gotoCartScreen = homeNavigationManager.gotoCartScreen
        )
    }
}
