package com.sample.datastoragemodule.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.sample.datastoragemodule.data.database.model.Wishlist

@Dao
interface WishlistDao {
    @Query("INSERT OR IGNORE INTO wishlist(productId) VALUES(:productId)")
    suspend fun addToWishlist(productId: String)

    @Query("DELETE FROM wishlist WHERE wishlist.productId = :productId")
    suspend fun removeFromWishlist(productId: String): Int

    @Query("SELECT * FROM wishlist")
    suspend fun getWishlistItems(): List<Wishlist>
}
