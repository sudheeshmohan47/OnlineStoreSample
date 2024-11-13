package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist

interface WishlistRepository {
    suspend fun addToWishlist(item: Wishlist)
    suspend fun getWishlistItems(onCompletion: (List<Wishlist>) -> Unit)
    suspend fun removeFromWishlist(productId: String, onCompletion: (List<Wishlist>) -> Unit)
}
