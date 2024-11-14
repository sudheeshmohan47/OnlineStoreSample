package com.sample.wishlistmodule.domain

import com.sample.datastoragemodule.data.database.model.Cart
import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.onlinestore.cartmodule.domain.CartRepository
import javax.inject.Inject

class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
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

    suspend fun addItemToCart(item: Wishlist, quantity: Int) {
        cartRepository.addToCart(
            Cart(
                productId = item.productId,
                name = item.name,
                price = item.price,
                image = item.image,
                description = item.description,
                quantity = quantity,
                category = item.category
            )
        )
    }
}
