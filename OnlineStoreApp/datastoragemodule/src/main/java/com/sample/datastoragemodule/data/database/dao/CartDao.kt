package com.sample.datastoragemodule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sample.datastoragemodule.data.database.model.Cart

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: Cart)

    @Update
    suspend fun updateCartItem(item: Cart)

    @Query("DELETE FROM cart WHERE cart.productId = :productId")
    suspend fun removeFromCart(productId: String): Int

    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<Cart>
}
