package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Wishlist
import javax.inject.Inject

class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) {
    suspend fun addToWishlist(productId: String, onCompletion: (Boolean) -> Unit) {
        wishlistRepository.addToWishlist(productId)
        onCompletion(true)
    }

    suspend fun getWishlistItems(): List<Wishlist> {
        return wishlistRepository.getWishlistItems()
    }

    suspend fun removeFromWishlist(
        productId: String,
        onCompletion: (Boolean, List<Wishlist>) -> Unit
    ) {
        wishlistRepository.removeFromWishlist(
            productId,
            onCompletion = { items ->
                onCompletion(true, items)
            }
        )
    }
}
