package com.sample.onlinestore.di
import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.data.splash.SplashService
import com.sample.onlinestore.domain.splash.SplashRepository
import com.sample.onlinestore.domain.splash.SplashUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StartupModule {
    @Provides
    fun provideSplashRepository(preferenceManagerRepository: PreferenceManagerRepository): SplashRepository {
        return SplashService(preferenceManagerRepository)
    }

    @Provides
    fun provideSplashUseCase(splashRepository: SplashRepository): SplashUseCase {
        return SplashUseCase(splashRepository)
    }
}
