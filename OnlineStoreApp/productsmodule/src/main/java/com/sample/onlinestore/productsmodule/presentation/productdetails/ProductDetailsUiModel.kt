package com.sample.onlinestore.productsmodule.presentation.productdetails

import androidx.annotation.StringRes
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event
import com.sample.onlinestore.productsmodule.domain.model.ProductItem

data class ProductDetailsUiModel(
    val product: ProductItem = ProductItem()
)

sealed class ProductDetailsAction : Action {
    data object RefreshData : ProductDetailsAction()
    data object OnClickFavourite : ProductDetailsAction()
    data object OnClickAddToCart : ProductDetailsAction()
    data object OnClickBackNavigation : ProductDetailsAction()
}

sealed class ProductDetailsEvent : Event {
    data object BackToPreviousScreen : ProductDetailsEvent()
    data object GotoCartScreen : ProductDetailsEvent()
    data class ShowMessage(val message: Message) : ProductDetailsEvent()
}
