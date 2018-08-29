package com.mashjulal.android.emailagent.data.repository.api

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Maybe
import io.reactivex.Single

interface AccountRepository {
    fun addUser(user: Account): Single<Long>
    fun getUserById(id: Long): Maybe<Account>
    fun getAll(): Single<List<Account>>
}