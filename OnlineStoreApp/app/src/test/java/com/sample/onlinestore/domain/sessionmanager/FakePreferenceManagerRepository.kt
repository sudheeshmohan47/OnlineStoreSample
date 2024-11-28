package com.sample.onlinestore.domain.sessionmanager

import com.sample.datastoragemodule.domain.PreferenceManagerRepository

class FakePreferenceManagerRepository: PreferenceManagerRepository {

    var appIntroFinishedStatus: Boolean = false
    var sessionTokenMock: String? = null

    override suspend fun saveAppIntroFinishedStatus(completed: Boolean) {
        appIntroFinishedStatus = completed
    }

    override suspend fun isAppIntroFinished(): Boolean {
        return appIntroFinishedStatus
    }

    override suspend fun saveUserSessionToken(sessionToken: String) {
        sessionTokenMock = sessionToken
    }

    override suspend fun getSessionToken(): String {
        return sessionTokenMock ?: ""
    }

    override suspend fun logoutUser() {
        sessionTokenMock = null
    }
}