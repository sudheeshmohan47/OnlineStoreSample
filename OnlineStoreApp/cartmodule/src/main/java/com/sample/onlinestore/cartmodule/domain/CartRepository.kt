package com.sample.onlinestore.cartmodule.domain

import com.sample.datastoragemodule.data.database.model.Cart

interface CartRepository {
    suspend fun addToCart(item: Cart)
    suspend fun getCartItems(): List<Cart>
    suspend fun removeFromCart(productId: String, onCompletion: (List<Cart>) -> Unit)
}
