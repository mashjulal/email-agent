package com.mashjulal.android.emailagent.data.repository.impl.folder

import com.mashjulal.android.emailagent.data.datasource.api.FolderDataSource
import com.mashjulal.android.emailagent.data.repository.api.FolderRepository
import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
        private val folderDataSource: FolderDataSource
): FolderRepository {
    override fun getAll(user: Account): Single<List<String>> {
        return folderDataSource.getAll(user)
    }
}