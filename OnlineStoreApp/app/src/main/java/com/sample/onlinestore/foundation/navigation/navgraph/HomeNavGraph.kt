package com.sample.onlinestore.foundation.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sample.onlinestore.categoriesmodule.presentation.categories.screen.CategoriesScreen
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
    categoriesScreenComposable()
}

private fun NavGraphBuilder.homeScreenComposable(
    homeNavigationManager: HomeNavigationManager
) {
    composable<BottomNavItem.Home> {
        ProductsListingScreen(
            loadProductDetailScreen = homeNavigationManager.gotoProductsDetailScreen
        )
    }
}

private fun NavGraphBuilder.categoriesScreenComposable() {
    composable<BottomNavItem.Categories> {
        CategoriesScreen()
    }
}
