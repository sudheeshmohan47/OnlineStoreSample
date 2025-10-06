package com.sample.onlinestore.categoriesmodule.domain

import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.categoriesmodule.domain.model.CategoryItem
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import javax.inject.Inject

/**
 * Use case for managing categories.
 *
 * Responsibilities:
 * - Update selected categories in the database.
 * - Fetch categories and map them to domain models.
 *
 * @param categoriesRepository Repository that handles category data operations.
 */
class CategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) {

    /**
     * Updates the selected categories in the database.
     *
     * Converts the provided list of [CategoryItem] objects into [SelectedCategory]
     * entities and updates the repository.
     *
     * @param selectedCategories List of categories selected by the user.
     */
    suspend fun updateSelectedCategories(selectedCategories: List<CategoryItem>) {
        val selectedCategoryList = selectedCategories.map {
            SelectedCategory(category = it.category)
        }
        categoriesRepository.updateSelectedCategories(selectedCategoryList)
    }

    /**
     * Fetches categories from the repository and maps them to domain models.
     *
     * @return [DomainResponse] containing a list of [CategoryItem] objects.
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
