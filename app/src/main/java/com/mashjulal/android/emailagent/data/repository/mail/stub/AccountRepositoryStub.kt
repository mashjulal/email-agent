package com.mashjulal.android.emailagent.data.repository.mail.stub

import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository

class AccountRepositoryStub: AccountRepository {

    private val data: MutableList<User> = mutableListOf()

    override fun getUserById(id: Long): User = data.find { it.id == id }!!

    override fun addUser(user: User): Long {
        val id = (data.size + 1).toLong()
        data.add(User(id, user.name, user.address, user.password))
        return id
    }
}