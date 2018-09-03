package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Single

interface AccountDataSource {

    fun insert(account: Account): Single<Long>

    fun update(account: Account): Completable

    fun getById(id: Long): Single<Account>
    fun getAll(): Single<List<Account>>


}