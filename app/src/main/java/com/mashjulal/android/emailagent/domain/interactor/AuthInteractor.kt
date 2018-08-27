package com.mashjulal.android.emailagent.domain.interactor

import io.reactivex.Completable

interface AuthInteractor {

    fun auth(address: String, password: String): Completable
}