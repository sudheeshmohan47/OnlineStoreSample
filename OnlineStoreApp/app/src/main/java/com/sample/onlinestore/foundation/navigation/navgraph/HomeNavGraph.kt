package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.slideComposable
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.navigation.navigationmanagers.HomeNavigationManager
import com.sample.onlinestore.presentation.dashboard.BottomNavItem
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductsListingScreen

fun NavGraphBuilder.homeNavGraph(
    appState: OnlineStoreAppState
) {
    val homeNavigationManager = HomeNavigationManager(appState)
    // HomeScreen
    homeScreenComposable(homeNavigationManager)
}

private fun NavGraphBuilder.homeScreenComposable(
    homeNavigationManager: HomeNavigationManager
) {
    slideComposable<BottomNavItem.Home> {
        ProductsListingScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen
        )
    }
}
