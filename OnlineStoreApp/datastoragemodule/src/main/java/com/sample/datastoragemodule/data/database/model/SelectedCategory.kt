package com.sample.datastoragemodule.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "selected_category",
    indices = [Index(value = ["category"], unique = true)]
)
data class SelectedCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String
)
