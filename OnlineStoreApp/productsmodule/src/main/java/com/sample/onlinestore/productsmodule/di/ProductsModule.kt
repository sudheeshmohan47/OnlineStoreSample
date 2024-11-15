package com.sample.onlinestore.productsmodule.di

import com.sample.onlinestore.productsmodule.data.ProductsService
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ProductsModule {

    @Provides
    fun provideProductsRepository(
        productsApiService: ProductsApiService
    ): ProductsRepository {
        return ProductsService(productsApiService)
    }

    @Provides
    fun provideProductsUseCase(productsRepository: ProductsRepository): ProductsUseCase {
        return ProductsUseCase(productsRepository)
    }

    @Provides
    fun provideProductsApiService(
        retrofit: Retrofit
    ): ProductsApiService {
        return retrofit.create(ProductsApiService::class.java)
    }
}
