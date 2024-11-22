package com.sample.onlinestore.cartmodule.di

import com.sample.datastoragemodule.data.database.dao.CartDao
import com.sample.onlinestore.cartmodule.data.CartService
import com.sample.onlinestore.cartmodule.data.api.CartApiService
import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.onlinestore.cartmodule.domain.CartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class CartModule {
    @Provides
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartService(cartDao)
    }

    @Provides
    fun provideCartUseCase(cartRepository: CartRepository): CartUseCase {
        return CartUseCase(cartRepository)
    }

    @Provides
    fun provideCartApiService(
        retrofit: Retrofit
    ): CartApiService {
        return retrofit.create(CartApiService::class.java)
    }
}
