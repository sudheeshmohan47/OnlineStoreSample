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

class CartService @Inject constructor(
    private val cartDao: CartDao,
    private val cartApiService: CartApiService
) :
    CartRepository {

    override suspend fun addToCart(item: CartRequest) {
        val cart = Cart(
            productId = item.productId,
            quantity = item.quantity
        )
        cartDao.addToCart(item = cart)
    }

    override suspend fun getCartItems(onCompletion: (Boolean, List<CartResponse>) -> Unit) {
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

                    // Pass the results to the onCompletion callback
                    onCompletion(true, cartItems)
                    return@getCartItems
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    override suspend fun removeFromCart(
        productId: String,
        onCompletion: (Boolean) -> Unit
    ) {
        if (cartDao.removeFromCart(productId) == 1) {
            onCompletion(true)
        }
    }
}
