package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.wishlistmodule.data.model.WishListResponse

interface WishlistRepository {
    suspend fun addToWishlist(productId: String)
    suspend fun getWishlistListingItems(): List<WishListResponse>
    suspend fun getWishlistItemsLocal(): List<Wishlist>
    suspend fun getWishlistItemsIds(): List<String>
    suspend fun moveItemToCart(productId: String)
    suspend fun removeFromWishlist(productId: String): Boolean
}
