package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Completable

interface AuthInteractor {

    fun auth(account: Account): Completable
}