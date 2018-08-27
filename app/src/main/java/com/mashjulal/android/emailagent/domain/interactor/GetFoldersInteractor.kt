package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Maybe

interface GetFoldersInteractor {
    fun getFoldersWithCurrent(folder: String): Maybe<Triple<List<String>, String, Int>>
    fun getFoldersWithForNewAccount(account: Account): Maybe<Triple<List<String>, String, Int>>
}