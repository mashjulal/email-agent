package com.mashjulal.android.emailagent.data.repository.impl.folder

import com.mashjulal.android.emailagent.data.repository.api.FolderRepository
import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single

interface FolderRepositoryFactory {
    fun createFolderRepository(account: Account) : Single<FolderRepository>
}