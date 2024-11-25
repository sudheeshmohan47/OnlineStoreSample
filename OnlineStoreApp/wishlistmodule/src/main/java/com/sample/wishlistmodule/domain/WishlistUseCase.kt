package com.sample.wishlistmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.wishlistmodule.domain.model.WishlistItem
import javax.inject.Inject

class WishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) {

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

    suspend fun moveItemToCart(productId: String, onCompletion: (Boolean) -> Unit) {
        wishlistRepository.moveItemToCart(productId)
        onCompletion(true)
    }
}
