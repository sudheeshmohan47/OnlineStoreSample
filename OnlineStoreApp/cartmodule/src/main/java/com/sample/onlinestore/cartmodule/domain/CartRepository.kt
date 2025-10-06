package com.sample.onlinestore.cartmodule.domain

import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.data.model.CartResponse

interface CartRepository {
    suspend fun addToCart(item: CartRequest)
    suspend fun getCartListingItems(): List<CartResponse>
    suspend fun getCartItemsLocal(): List<CartResponse>
    suspend fun removeFromCart(productId: String): Boolean
}
