package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface FolderDataSource {
    fun getAll(account: Account): Single<List<String>>
}