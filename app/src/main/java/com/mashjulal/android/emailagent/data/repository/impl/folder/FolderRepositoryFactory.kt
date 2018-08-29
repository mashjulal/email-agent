package com.mashjulal.android.emailagent.data.repository.impl.folder

import com.mashjulal.android.emailagent.data.repository.api.FolderRepository
import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Maybe

interface FolderRepositoryFactory {
    fun createFolderRepository(account: Account) : Maybe<FolderRepository>
}