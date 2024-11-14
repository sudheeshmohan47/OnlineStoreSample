package com.sample.onlinestore.cartmodule.domain

import com.sample.datastoragemodule.data.database.model.Cart

interface CartRepository {
    suspend fun addToCart(item: Cart)
    suspend fun getCartItems(onCompletion: (List<Cart>) -> Unit)
    suspend fun removeFromCart(productId: String, onCompletion: (List<Cart>) -> Unit)
}
