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
 * Responsibilities:
 * - Add, fetch, and remove items in the cart.
 * - Integrate with local database ([CartDao]) and remote API ([CartApiService]).
 * - Map errors and exceptions to domain-specific formats.
 *
 * @param cartDao DAO interface for performing local cart-related database operations.
 * @param cartApiService API service for fetching product data from the remote server.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class CartService @Inject constructor(
    private val cartDao: CartDao,
    private val cartApiService: CartApiService
) : CartRepository {

    /**
     * Adds an item to the cart.
     *
     * Converts a [CartRequest] to a [Cart] entity and stores it in the local database.
     *
     * @param item The item to be added to the cart.
     */
    override suspend fun addToCart(item: CartRequest) {
        val cart = Cart(
            productId = item.productId,
            quantity = item.quantity
        )
        cartDao.addToCart(cart)
    }

    /**
     * Fetches cart items from the database and supplements them with remote product details.
     *
     * Combines local cart items with remote product data to create a detailed list of [CartResponse].
     *
     * @return List of [CartResponse] representing the cart items with product details.
     * @throws Exception If API or mapping errors occur.
     */
    override suspend fun getCartListingItems(): List<CartResponse> {
        try {
            val response = cartApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
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
     * Retrieves locally stored cart items without remote product details.
     *
     * @return List of [CartResponse] representing the local cart items.
     */
    override suspend fun getCartItemsLocal(): List<CartResponse> = cartDao.getCartItems().map {
        CartResponse(
            productId = it.productId,
            quantity = it.quantity
        )
    }

    /**
     * Removes an item from the cart by product ID.
     *
     * @param productId The ID of the product to remove.
     * @return True if the item was removed successfully, false otherwise.
     */
    override suspend fun removeFromCart(productId: String): Boolean {
        return cartDao.removeFromCart(productId) == 1
    }
}
