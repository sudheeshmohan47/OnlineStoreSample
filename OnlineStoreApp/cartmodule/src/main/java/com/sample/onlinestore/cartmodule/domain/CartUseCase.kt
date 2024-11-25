package com.sample.onlinestore.cartmodule.domain

import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.domain.model.CartItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend fun addToWishlist(item: CartItem) {
        cartRepository.addToCart(CartRequest(productId = item.productId, quantity = item.quantity))
    }

    suspend fun getCartItems(onCompletion: (Boolean, DomainResponse<List<CartItem>>) -> Unit) {
        cartRepository.getCartListingItems { isSuccess, cartItems ->
            if (isSuccess) {
                val cartItemsList = cartItems.map { cartItem ->
                    CartItem(
                        productId = cartItem.productId ?: "",
                        quantity = cartItem.quantity ?: 0,
                        name = cartItem.name ?: "",
                        price = cartItem.price ?: 0.0,
                        category = cartItem.category ?: "",
                        description = cartItem.description ?: "",
                        image = cartItem.image ?: ""
                    )
                }
                onCompletion(true, DomainResponse(cartItemsList))
            }
        }
    }

    suspend fun removeFromCart(productId: String, onCompletion: (Boolean) -> Unit) {
        cartRepository.removeFromCart(productId, onCompletion)
    }
}
