package com.sample.onlinestore.foundation.navigation.navigationmanagers

import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState

class HomeNavigationManager(private val appState: OnlineStoreAppState) {
    val gotoProductsDetailScreen: (String) -> Unit = { productId ->
        // appState.navigate(ProductScreens.ProductDetailScreen)
    }
}