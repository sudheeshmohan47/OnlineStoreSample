package com.sample.onlinestore.cartmodule.data

import com.sample.datastoragemodule.data.database.dao.CartDao
import com.sample.datastoragemodule.data.database.model.Cart
import com.sample.onlinestore.cartmodule.domain.CartRepository
import javax.inject.Inject

class CartService @Inject constructor(private val cartDao: CartDao) :
    CartRepository {

    override suspend fun addToCart(item: Cart) {
        cartDao.addToCart(item = item)
    }

    override suspend fun getCartItems(onCompletion: (List<Cart>) -> Unit) {
        onCompletion(cartDao.getCartItems())
    }

    override suspend fun removeFromCart(productId: String, onCompletion: (List<Cart>) -> Unit) {
        if (cartDao.removeFromCart(productId) == 1) {
            getCartItems {
                onCompletion(it)
            }
        }
    }
}
