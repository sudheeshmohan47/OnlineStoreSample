package com.sample.datastoragemodule.domain

interface PreferenceManagerRepository {
    suspend fun saveAppIntroFinishedStatus(completed: Boolean)
    suspend fun isAppIntroFinished(): Boolean
    suspend fun saveUserSessionToken(sessionToken: String)
    suspend fun getSessionToken(): String
    suspend fun logoutUser()
}