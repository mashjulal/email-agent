package com.mashjulal.android.emailagent.data.repository.prefs

import android.content.SharedPreferences
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import javax.inject.Inject

private const val PREFS_LAST_USER_ID = "LAST_USER_ID"


private const val UNDEFINED = -1L
class PreferenceManagerImpl @Inject constructor(
        private val preferences: SharedPreferences
) : PreferenceManager {

    override fun getLastSelectedUserId(): Long
            = preferences.getLong(PREFS_LAST_USER_ID, UNDEFINED)

    override fun setLastSelectedUserId(userId: Long) {
        preferences.edit().putLong(PREFS_LAST_USER_ID, userId).apply()
    }

    override fun isAnyUserLogged(): Boolean
            = preferences.getLong(PREFS_LAST_USER_ID, UNDEFINED) != UNDEFINED
}