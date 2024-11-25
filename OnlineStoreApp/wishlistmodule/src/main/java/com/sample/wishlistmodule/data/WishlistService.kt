package com.sample.wishlistmodule.data

import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import com.sample.wishlistmodule.data.api.WishlistApiService
import com.sample.wishlistmodule.data.model.WishListResponse
import com.sample.wishlistmodule.domain.WishlistRepository
import javax.inject.Inject

class WishlistService @Inject constructor(
    private val wishlistDao: WishlistDao,
    private val cartRepository: CartRepository,
    private val wishlistApiService: WishlistApiService
) :
    WishlistRepository {
    override suspend fun addToWishlist(productId: String) {
        wishlistDao.addToWishlist(productId = productId)
    }

    override suspend fun getWishlistListingItems(onCompletion: (Boolean, List<WishListResponse>) -> Unit) {
        try {
            val response = wishlistApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    // Fetch cart items from the database and map them to CartResponse
                    val wishlistItems = wishlistDao.getWishlistItems().mapNotNull { wishlistItem ->
                        products.find { it.id == wishlistItem.productId }?.let { product ->
                            WishListResponse(
                                title = product.title,
                                productId = product.id.orEmpty(),
                                category = product.category.orEmpty(),
                                description = product.description.orEmpty(),
                                image = product.image.orEmpty(),
                                price = product.price
                            )
                        }
                    }

                    // Pass the results to the onCompletion callback
                    onCompletion(true, wishlistItems)
                    return@getWishlistListingItems
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    override suspend fun getWishlistItemsLocal(): List<Wishlist> = wishlistDao.getWishlistItems()

    override suspend fun moveItemToCart(productId: String) {
        wishlistDao.removeFromWishlist(productId)
        cartRepository.addToCart(
            CartRequest(
                productId = productId,
                quantity = 1
            )
        )
    }

    override suspend fun removeFromWishlist(
        productId: String,
        onCompletion: (Boolean) -> Unit
    ) {
        if (wishlistDao.removeFromWishlist(productId) == 1) {
            onCompletion(true)
        }
    }
}
