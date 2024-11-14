package com.sample.onlinestore.productsmodule.data.model

data class ProductResponse(
    val id: String,
    val name: String,
    val price: Double,
    val productId: String,
    val category: String,
    val description: String,
    val image: String,
)