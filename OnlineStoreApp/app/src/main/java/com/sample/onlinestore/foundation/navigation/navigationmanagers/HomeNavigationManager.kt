package com.sample.onlinestore.foundation.navigation.navigationmanagers

import com.sample.onlinestore.cartmodule.foundation.navigation.CartScreens
import com.sample.onlinestore.foundation.appstate.OnlineStoreAppState
import com.sample.onlinestore.foundation.appstate.navigate
import com.sample.onlinestore.foundation.appstate.popUp
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens

class HomeNavigationManager(private val appState: OnlineStoreAppState) {
    val gotoProductsDetailScreen: (String) -> Unit = { productId ->
         appState.navigate(ProductScreens.ProductDetailScreen(productId))
    }

    val backToPreviousScreen: () -> Unit = {
        appState.popUp()
    }

    val gotoCartScreen: () -> Unit = {
        //appState.navigate(CartScreens.CartScreen)
    }
}
