package com.sample.datastoragemodule.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wishlist",
    indices = [Index(value = ["productId"], unique = true)]
)
data class Wishlist(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String
)
