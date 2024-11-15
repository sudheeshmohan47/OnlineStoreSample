package com.sample.onlinestore.authenticationmodule.di

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import com.sample.onlinestore.authenticationmodule.data.LoginService
import com.sample.onlinestore.authenticationmodule.data.api.AuthenticationApiService
import com.sample.onlinestore.authenticationmodule.domain.LoginRepository
import com.sample.onlinestore.authenticationmodule.domain.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationModule {

    @Provides
    fun provideLoginRepository(
        authenticationApiService: AuthenticationApiService
    ): LoginRepository {
        return LoginService(authenticationApiService)
    }

    @Provides
    fun provideProductsUseCase(
        loginRepository: LoginRepository,
        preferenceManagerRepository: PreferenceManagerRepository
    ): LoginUseCase {
        return LoginUseCase(loginRepository, preferenceManagerRepository)
    }

    @Provides
    fun provideProductsApiService(
        retrofit: Retrofit
    ): AuthenticationApiService {
        return retrofit.create(AuthenticationApiService::class.java)
    }
}
