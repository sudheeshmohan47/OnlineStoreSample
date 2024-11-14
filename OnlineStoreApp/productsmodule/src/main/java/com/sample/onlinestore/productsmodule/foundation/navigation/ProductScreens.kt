package com.sample.onlinestore.productsmodule.foundation.navigation

import com.sample.onlinestore.commonmodule.foundation.base.BaseScreen
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
sealed class ProductScreens : BaseScreen() {

    @Serializable
    data object ProductScreen : ProductScreens()
}