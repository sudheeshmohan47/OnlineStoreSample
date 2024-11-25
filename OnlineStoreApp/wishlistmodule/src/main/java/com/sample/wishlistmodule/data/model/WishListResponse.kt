package com.sample.wishlistmodule.data.model

data class WishListResponse(
    val productId: String? = null,
    val title: String? = null,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    val isAddedToCart: Boolean = false,
    val price: Double? = 0.0
)
