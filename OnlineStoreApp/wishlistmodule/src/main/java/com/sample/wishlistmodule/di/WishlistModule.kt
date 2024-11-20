package com.sample.wishlistmodule.di

import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.wishlistmodule.data.WishlistService
import com.sample.wishlistmodule.domain.WishlistRepository
import com.sample.wishlistmodule.domain.WishlistUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WishlistModule {
    @Provides
    fun provideWishlistRepository(wishlistDao: WishlistDao): WishlistRepository {
        return WishlistService(wishlistDao)
    }

    @Provides
    fun provideWishlistUseCase(
        wishlistRepository: WishlistRepository
    ): WishlistUseCase {
        return WishlistUseCase(wishlistRepository)
    }
}
