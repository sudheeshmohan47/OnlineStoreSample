package com.sample.onlinestore.cartmodule.data.model

class CartResponse(
    val name: String? = "",
    val price: Double? = 0.0,
    val productId: String? = "",
    val category: String? = "",
    val description: String? = "",
    val image: String? = "",
    val quantity: Int? = 1
)
