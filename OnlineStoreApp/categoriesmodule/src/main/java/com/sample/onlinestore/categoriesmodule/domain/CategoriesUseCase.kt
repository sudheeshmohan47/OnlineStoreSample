package com.sample.onlinestore.categoriesmodule.domain

import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {
    suspend fun updateSelectedCategories(selectedCategories: List<CategoryItem>) {
        val selectedCategoryList = selectedCategories.map {
            SelectedCategory(category = it.category)
        }
        categoriesRepository.updateSelectedCategories(selectedCategoryList)
    }

    suspend fun fetchCategories(onCompletion: (Boolean, DomainResponse<List<CategoryItem>>) -> Unit) {
        categoriesRepository.fetchCategories { isSuccess, categoriesResponse ->
            if (isSuccess) {
                val categories = categoriesResponse.data?.map {
                    CategoryItem(
                        category = it.category,
                        isSelected = it.isSelected
                    )
                }
                onCompletion(true, DomainResponse(data = categories))
            }
        }
    }
}
