package com.sample.datastoragemodule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.sample.datastoragemodule.data.database.model.Cart

@Dao
interface CartDao {
    @Insert
    suspend fun addToCart(item: Cart)

    @Update
    suspend fun updateCartItem(item: Cart)

    @Delete
    suspend fun removeFromCart(item: Cart)

    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<Cart>
}
