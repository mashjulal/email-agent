package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.data.repository.mail.room.AccountDao
import com.mashjulal.android.emailagent.data.repository.mail.room.entity.AccountEntity
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
        private val accountDao: AccountDao
) : AccountRepository {

    override fun addUser(user: User): Long {
        return accountDao.insert(modelToEntity(user))
    }

    override fun getUserById(id: Long): User {
        return entityToModel(accountDao.getById(id))
    }

    private fun entityToModel(entity: AccountEntity): User =
            User(entity.id, entity.name, entity.email, entity.pwd)

    private fun modelToEntity(model: User): AccountEntity =
            AccountEntity(model.id, model.name, model.address, model.password)
}