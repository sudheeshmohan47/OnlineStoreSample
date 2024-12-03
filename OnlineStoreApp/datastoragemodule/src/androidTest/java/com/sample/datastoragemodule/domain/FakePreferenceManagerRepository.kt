package com.sample.datastoragemodule.domain

class FakePreferenceManagerRepository: PreferenceManagerRepository {
    var appIntroFinishedStatus = false
    var userSessionToken = ""

    override suspend fun saveAppIntroFinishedStatus(completed: Boolean) {
        appIntroFinishedStatus = completed
    }

    override suspend fun isAppIntroFinished(): Boolean = appIntroFinishedStatus

    override suspend fun saveUserSessionToken(sessionToken: String) {
        userSessionToken = sessionToken
    }

    override suspend fun getSessionToken(): String = userSessionToken

    override suspend fun logoutUser() {
        userSessionToken = ""
    }
}
