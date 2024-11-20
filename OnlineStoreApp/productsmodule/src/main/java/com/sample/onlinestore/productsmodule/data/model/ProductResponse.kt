package com.sample.onlinestore.productsmodule.data.model

data class ProductResponse(
    val id: String? = null,
    val title: String? = null,
    val price: Double? = null,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    val isWishListed: Boolean = false
)
