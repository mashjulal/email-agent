package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.User

interface AccountRepository {
    fun addUser(user: User): Long
    fun getUserById(id: Long): User
    fun getAll(): List<User>
}