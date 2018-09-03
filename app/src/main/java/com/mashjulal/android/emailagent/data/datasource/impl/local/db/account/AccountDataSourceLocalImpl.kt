package com.mashjulal.android.emailagent.data.datasource.impl.local.db.account

import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
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
            = { accountDao.insert(modelToEntity(account)) }.toIoCompletable()

    override fun insert(account: Account): Single<Long>
            = { accountDao.insert(modelToEntity(account)) }.toIoSingle()

    override fun getById(id: Long): Single<Account> =
            accountDao.getById(id).map { entityToModel(it) }

    override fun getAll(): Single<List<Account>>
            = accountDao.getAll()
                .flatMap { users -> { users.map { entityToModel(it) } }.toIoSingle()
    }

    private fun entityToModel(entity: AccountEntity): Account =
            Account(entity.id, entity.name, entity.email, entity.pwd)

    private fun modelToEntity(model: Account): AccountEntity =
            AccountEntity(model.id, model.name, model.address, model.password)
}