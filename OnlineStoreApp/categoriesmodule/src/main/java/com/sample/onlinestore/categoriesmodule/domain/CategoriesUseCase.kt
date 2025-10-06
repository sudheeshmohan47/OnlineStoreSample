package com.sample.onlinestore.categoriesmodule.domain

import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

/**
 * Use case for managing categories.
 * It provides methods to update selected categories in the database and
 * fetch categories from the repository.
 *
 * @param categoriesRepository Repository that handles category data operations.
 */
class CategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {
    /**
     * Updates the list of selected categories in the database.
     * Converts the given list of CategoryItem objects into a list of SelectedCategory objects
     * and then updates the repository.
     *
     * @param selectedCategories A list of CategoryItem objects that represent selected categories.
     */
    suspend fun updateSelectedCategories(selectedCategories: List<CategoryItem>) {
        val selectedCategoryList = selectedCategories.map {
            SelectedCategory(category = it.category)
        }
        categoriesRepository.updateSelectedCategories(selectedCategoryList)
    }

    /**
     * Fetches the categories from the repository and returns the result through a callback.
     * It maps the repository's response to a list of CategoryItem objects.
     *
     * @param onCompletion A callback that receives a success flag and a
     * DomainResponse containing a list of CategoryItem objects.
     */
    suspend fun fetchCategories(): DomainResponse<List<CategoryItem>> {
        val categoriesResponse = categoriesRepository.fetchCategories()
        return DomainResponse(
            data = categoriesResponse.data?.map {
                CategoryItem(
                    category = it.category,
                    isSelected = it.isSelected
                )
            }
        )
    }
}
