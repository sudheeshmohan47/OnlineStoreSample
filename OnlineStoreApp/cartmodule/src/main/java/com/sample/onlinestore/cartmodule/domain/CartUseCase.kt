package com.sample.onlinestore.cartmodule.domain

import com.sample.onlinestore.cartmodule.domain.model.CartItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

/**
 * Use case for managing cart operations.
 *
 * Provides methods to fetch and manipulate cart items, acting as an intermediary
 * between the UI layer and the repository.
 *
 * @param cartRepository Repository for performing cart-related operations.
 */
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    /**
     * Fetches cart items and provides them as domain response.
     *
     * @param onCompletion Callback with success status and the list of cart items.
     */
    suspend fun getCartItems(): DomainResponse<List<CartItem>> {
        val cartItems = cartRepository.getCartListingItems()

        return DomainResponse(
            data = cartItems.map { cartItem ->
                CartItem(
                    productId = cartItem.productId ?: "",
                    quantity = cartItem.quantity ?: 0,
                    name = cartItem.name ?: "",
                    price = cartItem.price ?: 0.0,
                    category = cartItem.category ?: "",
                    description = cartItem.description ?: "",
                    image = cartItem.image ?: ""
                )
            }
        )
    }

    /**
     * Removes an item from the cart by its product ID.
     *
     * @param productId The ID of the product to remove.
     * @param onCompletion Callback with success status.
     */
    suspend fun removeFromCart(productId: String): Boolean {
        return cartRepository.removeFromCart(productId)
    }
}
