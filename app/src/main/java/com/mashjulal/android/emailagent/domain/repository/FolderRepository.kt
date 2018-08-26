package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface FolderRepository {
    fun getAll(user: Account): Single<List<String>>
}

