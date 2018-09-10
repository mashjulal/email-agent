package com.mashjulal.android.emailagent.data.datasource.impl.local.db.account

import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.AccountMappers
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.utils.toIoCompletable
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AccountDataSourceLocalImpl @Inject constructor(
        private val accountDao: AccountDao
) : AccountDataSource {
    override fun update(account: Account): Completable
            = { accountDao.insert(AccountMappers.toAccountEntity(account)) }.toIoCompletable()

    override fun insert(account: Account): Single<Long>
            = { accountDao.insert(AccountMappers.toAccountEntity(account)) }.toIoSingle()

    override fun getById(id: Long): Single<Account> =
            accountDao.getById(id).map { AccountMappers.toAccountModel(it) }

    override fun getAll(): Single<List<Account>>
            = accountDao.getAll()
                .flatMap { users -> { users.map { AccountMappers.toAccountModel(it) } }.toIoSingle()
    }
}