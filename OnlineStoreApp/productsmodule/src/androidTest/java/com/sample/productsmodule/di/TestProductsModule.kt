package com.sample.productsmodule.di

import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.onlinestore.categoriesmodule.domain.CategoriesRepository
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.productsmodule.domain.FakeProductRepository
import com.sample.wishlistmodule.domain.WishlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class TestProductsModule {

    @Provides
    fun provideProductsRepository(
        productsApiService: ProductsApiService,
        wishlistRepository: WishlistRepository,
        categoriesRepository: CategoriesRepository,
        cartRepository: CartRepository,
    ): ProductsRepository {
        return FakeProductRepository()
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
