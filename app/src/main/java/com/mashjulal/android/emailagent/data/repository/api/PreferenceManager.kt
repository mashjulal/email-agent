package com.mashjulal.android.emailagent.data.repository.api

import io.reactivex.Completable
import io.reactivex.Single

interface PreferenceManager {

    fun getLastSelectedUserId(): Single<Long>
    fun setLastSelectedUserId(userId: Long): Completable
    fun isAnyUserLogged(): Single<Boolean>
}