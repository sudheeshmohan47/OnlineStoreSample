package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist

interface WishlistRepository {
    suspend fun addToWishlist(productId: String)
    suspend fun getWishlistItems(): List<Wishlist>
    suspend fun removeFromWishlist(productId: String, onCompletion: (List<Wishlist>) -> Unit)
}
