package com.sample.onlinestore.commonmodule.data.model.product

data class ProductResponse(
    val id: String? = null,
    val title: String? = null,
    val price: Double? = null,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    val isWishListed: Boolean = false,
    val isAddedToCart: Boolean = false
)
