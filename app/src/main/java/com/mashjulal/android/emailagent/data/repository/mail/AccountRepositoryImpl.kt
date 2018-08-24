package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.data.repository.mail.room.AccountDao
import com.mashjulal.android.emailagent.data.repository.mail.room.entity.AccountEntity
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
        private val accountDao: AccountDao
) : AccountRepository {

    override fun addUser(user: User): Single<Long> {
        return { accountDao.insert(modelToEntity(user)) }.toIoSingle()
    }

    override fun getUserById(id: Long): Maybe<User> {
        return accountDao.getById(id).map { entityToModel(it) }
    }

    override fun getAll(): Single<List<User>> {
        return accountDao.getAll()
                .flatMap { users -> Single.fromCallable { users.map { entityToModel(it) } } }
    }

    private fun entityToModel(entity: AccountEntity): User =
            User(entity.id, entity.name, entity.email, entity.pwd)

    private fun modelToEntity(model: User): AccountEntity =
            AccountEntity(model.id, model.name, model.address, model.password)
}