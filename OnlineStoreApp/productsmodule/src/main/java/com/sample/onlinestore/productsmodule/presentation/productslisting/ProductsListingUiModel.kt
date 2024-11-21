package com.sample.onlinestore.productsmodule.presentation.productslisting

import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event
import com.sample.onlinestore.productsmodule.domain.model.ProductItem

data class ProductsListingUiModel(
    val products: List<ProductItem> = emptyList(),
    val isInitialLoadingCompleted: Boolean = false,
    val isSwipeRefreshing: Boolean = false
)

sealed class ProductsListingAction : Action {
    data class OnClickProduct(val product: ProductItem) : ProductsListingAction()
    data class SetSwipeRefreshingStatus(val isRefreshing: Boolean) : ProductsListingAction()
    data object RefreshData : ProductsListingAction()
    data class OnClickFavourite(val product: ProductItem) : ProductsListingAction()
}

sealed class ProductsListingEvent : Event {
    data class LoadProductDetailScreen(val productId: String) : ProductsListingEvent()
}
