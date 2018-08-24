package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.User
import io.reactivex.Maybe
import io.reactivex.Single

interface AccountRepository {
    fun addUser(user: User): Single<Long>
    fun getUserById(id: Long): Maybe<User>
    fun getAll(): Single<List<User>>
}