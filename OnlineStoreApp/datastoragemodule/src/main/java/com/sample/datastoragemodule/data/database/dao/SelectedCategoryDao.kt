package com.sample.datastoragemodule.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.datastoragemodule.data.database.model.SelectedCategory

@Dao
interface SelectedCategoryDao {

    @Query("DELETE FROM selected_category")
    suspend fun clearSelectedCategories()

    // Insert or update multiple categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedCategories(categories: List<SelectedCategory>)

    @Query("SELECT * FROM selected_category")
    suspend fun getSelectedCategories(): List<SelectedCategory>
}
