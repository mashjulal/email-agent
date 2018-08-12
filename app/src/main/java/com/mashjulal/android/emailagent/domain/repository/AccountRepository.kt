package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.User

interface AccountRepository {
    fun getUserById(id: Long): User
}