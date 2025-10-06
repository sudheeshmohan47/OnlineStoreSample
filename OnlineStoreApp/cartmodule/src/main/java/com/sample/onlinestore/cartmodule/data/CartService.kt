package com.sample.onlinestore.cartmodule.data

import com.sample.datastoragemodule.data.database.dao.CartDao
import com.sample.datastoragemodule.data.database.model.Cart
import com.sample.onlinestore.cartmodule.data.api.CartApiService
import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.data.model.CartResponse
import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import javax.inject.Inject

/**
 * Service class for managing cart operations in the online store module.
 *
 * This class integrates with the local database (`CartDao`) and remote API (`CartApiService`)
 * to handle adding, fetching, and removing items in the cart. It ensures error handling
 * and synchronization between local and remote data sources.
 *
 * @param cartDao DAO interface for performing local cart-related database operations.
 * @param cartApiService API service for fetching product data from the remote server.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class CartService @Inject constructor(
    private val cartDao: CartDao,
    private val cartApiService: CartApiService
) :
    CartRepository {

    /**
     * Adds an item to the cart.
     *
     * Converts a `CartRequest` to a `Cart` entity and stores it in the local database.
     *
     * @param item The item to be added to the cart.
     */
    override suspend fun addToCart(item: CartRequest) {
        val cart = Cart(
            productId = item.productId,
            quantity = item.quantity
        )
        cartDao.addToCart(item = cart)
    }

    /**
     * Fetches cart listing items from the database and supplements them with remote API product details.
     *
     * Combines locally stored cart items with remote product details to build a comprehensive cart list.
     *
     * @param onCompletion Callback to return the success status and the list of `CartResponse` items.
     *                     If successful, returns `true` and the list of cart items.
     *                     On failure, throws an exception.
     */
    override suspend fun getCartListingItems(): List<CartResponse> {
        try {
            val response = cartApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    // Fetch cart items from the database and map them to CartResponse
                    val cartItems = cartDao.getCartItems().mapNotNull { cartItem ->
                        products.find { it.id == cartItem.productId }?.let { product ->
                            CartResponse(
                                quantity = cartItem.quantity,
                                name = product.title,
                                price = product.price ?: 0.0,
                                productId = product.id.orEmpty(),
                                category = product.category.orEmpty(),
                                description = product.description.orEmpty(),
                                image = product.image.orEmpty()
                            )
                        }
                    }
                    return cartItems
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Retrieves locally stored cart items.
     *
     * Maps database `Cart` entities to `CartResponse` objects.
     *
     * @return A list of `CartResponse` objects representing cart items.
     */
    override suspend fun getCartItemsLocal(): List<CartResponse> = cartDao.getCartItems().map {
        CartResponse(
            productId = it.productId,
            quantity = it.quantity
        )
    }

    /**
     * Removes an item from the cart by its product ID.
     *
     * Invokes a callback with the success status.
     *
     * @param productId The ID of the product to be removed.
     * @param onCompletion Callback to indicate whether the item was successfully removed.
     */
    override suspend fun removeFromCart(
        productId: String
    ): Boolean {
        return cartDao.removeFromCart(productId) == 1
    }
}
