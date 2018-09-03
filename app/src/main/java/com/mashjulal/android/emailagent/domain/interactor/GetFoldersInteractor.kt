package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface GetFoldersInteractor {
    fun getFoldersWithCurrent(folder: String): Single<Triple<List<String>, String, Int>>
    fun getFoldersWithForNewAccount(account: Account): Single<Triple<List<String>, String, Int>>
}