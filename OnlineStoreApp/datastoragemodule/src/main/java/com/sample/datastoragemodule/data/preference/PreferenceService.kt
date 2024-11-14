package com.sample.datastoragemodule.data.preference

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferenceService @Inject constructor(private val preferenceManager: PreferenceManager) :
    PreferenceManagerRepository {
    override suspend fun saveAppIntroFinishedStatus(completed: Boolean) {
        preferenceManager.saveAppIntroStatus(completed)
    }

    override suspend fun saveUserSessionToken(sessionToken: String) {
        preferenceManager.storeSessionToken(sessionToken)
    }

    override suspend fun getSessionToken(): String =
        preferenceManager.getSessionToken().first().orEmpty()

    override suspend fun logoutUser() {
        preferenceManager.endUserSession()
    }
}