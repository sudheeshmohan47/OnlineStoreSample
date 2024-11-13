package com.sample.datastoragemodule.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sample.datastoragemodule.data.model.Wishlist

@Dao
interface WishlistDao {
    @Insert
    suspend fun addToWishlist(item: Wishlist)

    @Delete
    suspend fun removeFromWishlist(item: Wishlist)

    @Query("SELECT * FROM wishlist")
    suspend fun getWishlistItems(): List<Wishlist>
}
