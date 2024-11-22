package com.sample.onlinestore.cartmodule.presentation.cart

import com.sample.onlinestore.cartmodule.domain.model.CartItem
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event

data class CartUiModel(
    val cartItems: List<CartItem> = emptyList()
)

sealed class CartAction : Action {
    data class OnClickProduct(val productId: String) : CartAction()
    data object OnClickBackNavigation : CartAction()
    data class OnClickCartImage(val productId: String) : CartAction()
    data class RemoveItemFromCart(val productId: String) : CartAction()
    data object RefreshData : CartAction()
}

sealed class CartEvent : Event {
    data class LoadProductDetailScreen(val productId: String) : CartEvent()
    data class ShowMessage(val message: Message) : CartEvent()
}
