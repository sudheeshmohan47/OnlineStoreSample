package com.sample.onlinestore.categoriesmodule.domain

import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend fun updateSelectedCategories(selectedCategories: List<SelectedCategory>) {
        categoriesRepository.updateSelectedCategories(selectedCategories)
    }

    suspend fun getSelectedCategories(onCompletion: (List<SelectedCategory>) -> Unit) {
        categoriesRepository.getSelectedCategories(onCompletion)
    }

    suspend fun fetchCategories(onCompletion: (Boolean, DomainResponse<List<String>>) -> Unit) {
        categoriesRepository.fetchCategories(onCompletion)
    }
}
