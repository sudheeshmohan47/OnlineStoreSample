package com.sample.datastoragemodule.data.preference

import com.sample.datastoragemodule.domain.PreferenceManagerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferenceManagerService @Inject constructor(private val preferenceManager: PreferenceManager) :
    PreferenceManagerRepository {
    override suspend fun saveAppIntroFinishedStatus(completed: Boolean) {
        preferenceManager.saveAppIntroStatus(completed)
    }

    override suspend fun isAppIntroFinished(): Boolean = preferenceManager.getAppIntroStatus()

    override suspend fun saveUserSessionToken(sessionToken: String) {
        preferenceManager.storeSessionToken(sessionToken)
    }

    override suspend fun getSessionToken(): String =
        preferenceManager.getSessionToken().orEmpty()

    override suspend fun logoutUser() {
        preferenceManager.endUserSession()
    }
}
