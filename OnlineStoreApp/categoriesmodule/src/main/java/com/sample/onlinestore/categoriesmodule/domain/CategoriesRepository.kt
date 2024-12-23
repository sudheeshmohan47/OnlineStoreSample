package com.sample.onlinestore.categoriesmodule.domain

import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.categoriesmodule.data.model.CategoriesResponse
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse

interface CategoriesRepository {
    suspend fun updateSelectedCategories(selectedCategories: List<SelectedCategory>)
    suspend fun fetchCategories(onCompletion: (Boolean, DomainResponse<List<CategoriesResponse>>) -> Unit)
    suspend fun getSelectedCategories(): List<String>
}
