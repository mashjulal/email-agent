package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface GetAccountsWithCurrentInteractor {
    fun get(): Single<Triple<List<Account>, Account, Int>>
    fun get(accountId: Long): Single<Triple<List<Account>, Account, Int>>
}