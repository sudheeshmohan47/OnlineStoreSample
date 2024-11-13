package com.sample.wishlistmodule.data

import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.wishlistmodule.domain.WishlistRepository
import javax.inject.Inject

class WishlistService @Inject constructor(private val wishlistDao: WishlistDao) :
    WishlistRepository {
    override suspend fun addToWishlist(item: Wishlist) {
        wishlistDao.addToWishlist(item = item)
    }

    override suspend fun getWishlistItems(onCompletion: (List<Wishlist>) -> Unit) {
        onCompletion(wishlistDao.getWishlistItems())
    }

    override suspend fun removeFromWishlist(productId: String, onCompletion: (List<Wishlist>) -> Unit) {
        if (wishlistDao.removeFromWishlist(productId) == 1) {
            getWishlistItems {
                onCompletion(it)
            }
        }
    }
}
