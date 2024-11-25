package com.sample.wishlistmodule.di

import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.wishlistmodule.data.WishlistService
import com.sample.wishlistmodule.data.api.WishlistApiService
import com.sample.wishlistmodule.domain.WishlistRepository
import com.sample.wishlistmodule.domain.WishlistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class WishlistModule {
    @Provides
    fun provideWishlistRepository(
        wishlistDao: WishlistDao,
        cartRepository: CartRepository,
        wishlistApiService: WishlistApiService
    ): WishlistRepository {
        return WishlistService(wishlistDao, cartRepository, wishlistApiService)
    }

    @Provides
    fun provideWishlistUseCase(
        wishlistRepository: WishlistRepository
    ): WishlistUseCase {
        return WishlistUseCase(wishlistRepository)
    }

    @Provides
    fun provideWishlistApiService(
        retrofit: Retrofit
    ): WishlistApiService {
        return retrofit.create(WishlistApiService::class.java)
    }
}
