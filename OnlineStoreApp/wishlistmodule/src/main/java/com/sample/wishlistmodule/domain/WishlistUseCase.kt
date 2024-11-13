package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist
import javax.inject.Inject

class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    suspend fun addToWishlist(item: Wishlist) {
        wishlistRepository.addToWishlist(item)
    }
    suspend fun getWishlistItems(onCompletion: (List<Wishlist>) -> Unit) {
        wishlistRepository.getWishlistItems(onCompletion)
    }

    suspend fun removeFromWishlist(productId: String, onCompletion: (List<Wishlist>) -> Unit) {
        wishlistRepository.removeFromWishlist(productId, onCompletion)
    }
}
