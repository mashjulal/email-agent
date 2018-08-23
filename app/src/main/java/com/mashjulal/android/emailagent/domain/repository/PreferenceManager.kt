package com.mashjulal.android.emailagent.domain.repository

interface PreferenceManager {

    fun getLastSelectedUserId(): Long
    fun setLastSelectedUserId(userId: Long)
    fun isAnyUserLogged(): Boolean
}