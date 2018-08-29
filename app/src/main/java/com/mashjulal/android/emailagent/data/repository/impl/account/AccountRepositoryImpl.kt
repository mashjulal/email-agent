package com.mashjulal.android.emailagent.data.repository.impl.account

import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
        private val accountSource: AccountDataSource
) : AccountRepository {

    override fun addUser(user: Account): Single<Long>
            = accountSource.insert(user)

    override fun getUserById(id: Long): Maybe<Account>
            = accountSource.getById(id)

    override fun getAll(): Single<List<Account>>
            = accountSource.getAll()
}