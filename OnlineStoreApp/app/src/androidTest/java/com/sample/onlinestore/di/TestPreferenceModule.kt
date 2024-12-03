package com.sample.onlinestore.di

import android.content.Context
import com.sample.datastoragemodule.data.preference.PreferenceManager
import com.sample.datastoragemodule.data.preference.PreferenceManagerService
import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestPreferenceModule {

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }
    @Provides
    @Singleton
    fun providePreferenceManagerRepository(preferenceManager: PreferenceManager): PreferenceManagerRepository {
        return PreferenceManagerService(preferenceManager)
    }
}
