package com.sample.wishlistmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.wishlistmodule.domain.model.WishlistItem
import javax.inject.Inject

/**
 * Use case class for managing wishlist operations, interacting with the repository
 * to fetch, remove, or move items in the wishlist.
 *
 * @param wishlistRepository Repository for handling wishlist-related data operations.
 */
class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) {

    /**
     * Fetches the list of items in the wishlist and maps them to the WishlistItem model.
     *
     * @param onCompletion A callback that returns a boolean indicating success or failure,
     * and a DomainResponse containing the list of WishlistItem.
     */
    suspend fun getWishlistItems(onCompletion: (Boolean, DomainResponse<List<WishlistItem>>) -> Unit) {
        wishlistRepository.getWishlistListingItems { isSuccess, wishlistItems ->
            if (isSuccess) {
                val cartItemsList = wishlistItems.map { wishlistItem ->
                    WishlistItem(
                        productId = wishlistItem.productId ?: "",
                        name = wishlistItem.title ?: "",
                        category = wishlistItem.category ?: "",
                        description = wishlistItem.description ?: "",
                        image = wishlistItem.image ?: "",
                        price = wishlistItem.price ?: 0.0
                    )
                }
                onCompletion(true, DomainResponse(cartItemsList))
            }
        }
    }

    /**
     * Removes an item from the wishlist.
     *
     * @param productId The ID of the product to be removed from the wishlist.
     * @param onCompletion A callback that returns a boolean indicating whether the removal
     * operation was successful.
     */
    suspend fun removeFromWishlist(
        productId: String,
        onCompletion: (Boolean) -> Unit
    ) {
        wishlistRepository.removeFromWishlist(
            productId,
            onCompletion = { isSuccess ->
                onCompletion(isSuccess)
            }
        )
    }

    /**
     * Removes an item from the wishlist.
     *
     * @param productId The ID of the product to be removed from the wishlist.
     * @param onCompletion A callback that returns a boolean indicating whether the removal
     * operation was successful.
     */
    suspend fun moveItemToCart(productId: String, onCompletion: (Boolean) -> Unit) {
        wishlistRepository.moveItemToCart(productId)
        onCompletion(true)
    }
}
