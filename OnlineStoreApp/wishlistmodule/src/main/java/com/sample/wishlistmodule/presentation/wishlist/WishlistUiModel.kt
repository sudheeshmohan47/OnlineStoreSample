package com.sample.wishlistmodule.presentation.wishlist

import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event
import com.sample.wishlistmodule.domain.model.WishlistItem

data class WishlistUiModel(
    val wishListedProducts: List<WishlistItem> = emptyList()
)

sealed class WishlistAction : Action {
    data class OnClickWishListedItem(val productId: String) : WishlistAction()
    data object RefreshData : WishlistAction()
    data class OnClickRemoveFromWishlist(val productId: String) : WishlistAction()
    data class OnClickMoveToCart(val productId: String) : WishlistAction()
    data object OnClickCart : WishlistAction()
}

sealed class WishlistEvent : Event {
    data class LoadProductDetailScreen(val productId: String) : WishlistEvent()
    data object GotoCartScreen : WishlistEvent()
    data class ShowMessage(val message: Message) : WishlistEvent()
}
