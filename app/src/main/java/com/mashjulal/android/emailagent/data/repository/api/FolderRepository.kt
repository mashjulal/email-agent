package com.mashjulal.android.emailagent.data.repository.api

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface FolderRepository {
    fun getAll(user: Account): Single<List<String>>
}

