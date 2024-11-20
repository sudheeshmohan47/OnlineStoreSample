package com.sample.onlinestore.productsmodule.domain.model

data class ProductItem(
    val name: String? = null,
    val price: Double? = null,
    val productId: String,
    val isWishListed: Boolean = false,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null
)
