package com.sample.onlinestore.cartmodule.domain.model

data class CartItem(
    val name: String,
    val price: Double,
    val productId: String,
    val category: String,
    val description: String,
    val image: String
)
