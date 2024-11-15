package com.sample.datastoragemodule.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferenceManager.SESSION_PREFERENCES)

class PreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        const val SESSION_PREFERENCES = "SESSION_PREFERENCES"
        const val SESSION_TOKEN = "SESSION_TOKEN"
        const val APP_INTRO_STATUS = "APP_INTRO_STATUS"

    }

    suspend fun getAppIntroStatus(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(APP_INTRO_STATUS)]  ?: false
        }.first()
    }

    suspend fun getSessionToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(SESSION_TOKEN)].orEmpty()
        }.first()
    }

    suspend fun storeSessionToken(token: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(SESSION_TOKEN)] = token
        }
    }

    suspend fun saveAppIntroStatus(isAppIntroFinished: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(APP_INTRO_STATUS)] = isAppIntroFinished
        }
    }

    suspend fun endUserSession() {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(SESSION_TOKEN)] = ""
        }
    }
}
