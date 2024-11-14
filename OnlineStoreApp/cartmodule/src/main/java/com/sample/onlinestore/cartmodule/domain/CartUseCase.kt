package com.sample.onlinestore.cartmodule.domain

import com.sample.datastoragemodule.data.database.model.Cart
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend fun addToWishlist(item: Cart) {
        cartRepository.addToCart(item)
    }

    suspend fun getCartItems(onCompletion: (List<Cart>) -> Unit) {
        cartRepository.getCartItems(onCompletion)
    }

    suspend fun removeFromCart(productId: String, onCompletion: (List<Cart>) -> Unit) {
        cartRepository.removeFromCart(productId, onCompletion)
    }
}
