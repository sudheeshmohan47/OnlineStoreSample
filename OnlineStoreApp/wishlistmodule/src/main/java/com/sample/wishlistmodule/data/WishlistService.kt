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

/**
 * Service class for managing wishlist operations, interacting with local storage,
 * remote API, and the cart repository to add, remove, fetch wishlist items, and move
 * items to the cart.
 *
 * @param wishlistDao DAO interface for performing local wishlist database operations.
 * @param cartRepository Repository for handling cart operations.
 * @param wishlistApiService API service for fetching product data from the remote server.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class WishlistService @Inject constructor(
    private val wishlistDao: WishlistDao,
    private val cartRepository: CartRepository,
    private val wishlistApiService: WishlistApiService
) :
    WishlistRepository {

    /**
     * Adds a product to the wishlist.
     *
     * @param productId The ID of the product to be added to the wishlist.
     */
    override suspend fun addToWishlist(productId: String) {
        wishlistDao.addToWishlist(productId = productId)
    }

    /**
     * Fetches the list of products for the wishlist from the remote server and maps them
     * to the local wishlist items.
     *
     * @param onCompletion A callback to return the success status and a list of wishlist items.
     */
    override suspend fun getWishlistListingItems(): List<WishListResponse> {
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
                    return wishlistItems
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Retrieves the list of wishlist items from the local database.
     *
     * @return A list of wishlist items.
     */
    override suspend fun getWishlistItemsLocal(): List<Wishlist> = wishlistDao.getWishlistItems()

    /**
     * Retrieves the list of wishlist items from the local database and returns their IDs.
     *
     * @return A list of ids of wishListed items.
     */
    override suspend fun getWishlistItemsIds(): List<String> {
        return getWishlistItemsLocal().map { it.productId }
    }

    /**
     * Moves an item from the wishlist to the cart.
     *
     * @param productId The ID of the product to be moved from wishlist to cart.
     */
    override suspend fun moveItemToCart(productId: String) {
        wishlistDao.removeFromWishlist(productId)
        cartRepository.addToCart(
            CartRequest(
                productId = productId,
                quantity = 1
            )
        )
    }

    /**
     * Removes a product from the wishlist.
     *
     * @param productId The ID of the product to be removed from the wishlist.
     * @param onCompletion A callback to return the success status of the removal operation.
     */
    override suspend fun removeFromWishlist(
        productId: String
    ): Boolean {
        return wishlistDao.removeFromWishlist(productId) == 1
    }
}
