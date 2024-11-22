package com.sample.onlinestore.cartmodule.domain

import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.data.model.CartResponse

interface CartRepository {
    suspend fun addToCart(item: CartRequest)
    suspend fun getCartItems(onCompletion: (Boolean, List<CartResponse>) -> Unit)
    suspend fun removeFromCart(
        productId: String, onCompletion: (Boolean) -> Unit
    )
}
