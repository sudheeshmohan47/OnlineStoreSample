package com.sample.datastoragemodule.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart",
    indices = [Index(value = ["productId"], unique = true)]
)
data class Cart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int,
    val productId: String
)
