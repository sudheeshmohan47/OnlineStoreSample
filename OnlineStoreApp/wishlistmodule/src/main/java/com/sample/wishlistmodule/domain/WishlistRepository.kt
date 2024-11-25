package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.wishlistmodule.data.model.WishListResponse

interface WishlistRepository {
    suspend fun addToWishlist(productId: String)
    suspend fun getWishlistListingItems(onCompletion: (Boolean, List<WishListResponse>) -> Unit)
    suspend fun getWishlistItemsLocal(): List<Wishlist>
    suspend fun moveItemToCart(productId: String)
    suspend fun removeFromWishlist(productId: String, onCompletion: (Boolean) -> Unit)
}
