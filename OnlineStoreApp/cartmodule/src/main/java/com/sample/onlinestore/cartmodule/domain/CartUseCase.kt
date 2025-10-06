package com.sample.onlinestore.cartmodule.domain

import com.sample.onlinestore.cartmodule.domain.model.CartItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

/**
 * Use case for managing cart operations.
 *
 * Responsibilities:
 * - Fetch and map cart items from the repository.
 * - Remove items from the cart.
 *
 * Acts as an intermediary between the UI layer and [CartRepository].
 *
 * @param cartRepository Repository for performing cart-related operations.
 */
class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    /**
     * Fetches cart items and maps them to domain models.
     *
     * @return [DomainResponse] containing a list of [CartItem] objects.
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
     * Removes an item from the cart by product ID.
     *
     * @param productId The ID of the product to remove.
     * @return True if the item was removed successfully, false otherwise.
     */
    suspend fun removeFromCart(productId: String): Boolean {
        return cartRepository.removeFromCart(productId)
    }
}
