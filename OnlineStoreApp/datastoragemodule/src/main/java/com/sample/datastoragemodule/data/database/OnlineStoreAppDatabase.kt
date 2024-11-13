package com.sample.datastoragemodule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.datastoragemodule.data.database.dao.CartDao
import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.datastoragemodule.data.model.Cart
import com.sample.datastoragemodule.data.model.Wishlist

@Database(entities = [Wishlist::class, Cart::class], version = 1)
abstract class OnlineStoreAppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun cartDao(): CartDao
}
