package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Maybe
import io.reactivex.Single

interface AccountDataSource {

    fun insert(account: Account): Single<Long>
    fun getById(id: Long): Maybe<Account>
    fun getAll(): Single<List<Account>>
}