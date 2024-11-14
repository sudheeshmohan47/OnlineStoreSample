package com.sample.onlinestore.categoriesmodule.di

import com.sample.datastoragemodule.data.database.dao.SelectedCategoryDao
import com.sample.onlinestore.categoriesmodule.data.CategoriesService
import com.sample.onlinestore.categoriesmodule.data.api.CategoriesApiService
import com.sample.onlinestore.categoriesmodule.domain.CategoriesRepository
import com.sample.onlinestore.categoriesmodule.domain.CategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class CategoriesModule {
    @Provides
    fun provideCategoriesRepository(
        selectedCategoryDao: SelectedCategoryDao,
        categoriesApiService: CategoriesApiService
    ): CategoriesRepository {
        return CategoriesService(selectedCategoryDao, categoriesApiService)
    }

    @Provides
    fun provideCategoryUseCase(categoriesRepository: CategoriesRepository): CategoriesUseCase {
        return CategoriesUseCase(categoriesRepository)
    }

    @Provides
    fun provideCategoriesApiService(
        retrofit: Retrofit
    ): CategoriesApiService {
        return retrofit.create(CategoriesApiService::class.java)
    }
}
