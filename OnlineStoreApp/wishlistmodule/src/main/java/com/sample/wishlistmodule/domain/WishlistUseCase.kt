package com.sample.wishlistmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.wishlistmodule.domain.model.WishlistItem
import javax.inject.Inject

/**
 * Use case for managing wishlist operations.
 *
 * Responsibilities:
 * - Fetch wishlist items and map them to domain models.
 * - Remove items from the wishlist.
 * - Move items from the wishlist to the cart.
 *
 * Acts as an intermediary between the UI layer and [WishlistRepository].
 *
 * @param wishlistRepository Repository for handling wishlist-related data operations.
 */
class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) {

    /**
     * Fetches the list of items in the wishlist and maps them to [WishlistItem].
     *
     * @return [DomainResponse] containing the list of [WishlistItem].
     */
    suspend fun getWishlistItems(): DomainResponse<List<WishlistItem>> {
        val wishlistItemsList = wishlistRepository.getWishlistListingItems()
        return DomainResponse(
            wishlistItemsList.map { wishlistItem ->
                WishlistItem(
                    productId = wishlistItem.productId ?: "",
                    name = wishlistItem.title ?: "",
                    category = wishlistItem.category ?: "",
                    description = wishlistItem.description ?: "",
                    image = wishlistItem.image ?: "",
                    price = wishlistItem.price ?: 0.0
                )
            }
        )
    }

    /**
     * Removes an item from the wishlist by product ID.
     *
     * @param productId The ID of the product to remove.
     * @return True if the item was successfully removed, false otherwise.
     */
    suspend fun removeFromWishlist(productId: String): Boolean {
        return wishlistRepository.removeFromWishlist(productId)
    }

    /**
     * Moves an item from the wishlist to the cart.
     *
     * @param productId The ID of the product to move.
     * @return True if the operation was successful.
     */
    suspend fun moveItemToCart(productId: String): Boolean {
        wishlistRepository.moveItemToCart(productId)
        return true
    }
}
