package com.sample.onlinestore.categoriesmodule.data

import com.sample.datastoragemodule.data.database.dao.SelectedCategoryDao
import com.sample.datastoragemodule.data.database.model.SelectedCategory
import com.sample.onlinestore.categoriesmodule.data.api.CategoriesApiService
import com.sample.onlinestore.categoriesmodule.domain.CategoriesRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import javax.inject.Inject

@SuppressWarnings("TooGenericExceptionCaught")
class CategoriesService @Inject constructor(
    private val categoryDao: SelectedCategoryDao,
    private val categoriesApiService: CategoriesApiService
) :
    CategoriesRepository {
    override suspend fun updateSelectedCategories(selectedCategories: List<SelectedCategory>) {
        categoryDao.clearSelectedCategories()
        categoryDao.insertSelectedCategories(categories = selectedCategories)
    }

    override suspend fun getSelectedCategories(onCompletion: (List<SelectedCategory>) -> Unit) {
        onCompletion(categoryDao.getSelectedCategories())
    }

    override suspend fun fetchCategories(onCompletion: (Boolean, DomainResponse<List<String>>) -> Unit) {
        try {
            val response = categoriesApiService.getProductCategories()
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    onCompletion(true, DomainResponse(data = categories))
                    return@fetchCategories
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }
}