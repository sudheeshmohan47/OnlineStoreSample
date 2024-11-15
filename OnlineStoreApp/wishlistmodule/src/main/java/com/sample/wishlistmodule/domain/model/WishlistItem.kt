package com.sample.wishlistmodule.domain.model

data class WishlistItem(
    val name: String,
    val price: Double,
    val productId: String,
    val category: String,
    val description: String,
    val image: String
)
