package com.sample.onlinestore.productsmodule.domain.model

data class ProductItem(
    val id: String,
    val name: String,
    val price: Double,
    val productId: String,
    val category: String,
    val description: String,
    val image: String
)
