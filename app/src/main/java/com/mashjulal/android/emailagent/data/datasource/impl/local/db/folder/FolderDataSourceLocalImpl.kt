package com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder

import com.mashjulal.android.emailagent.data.datasource.api.FolderDataSource
import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Single
import javax.inject.Inject

class FolderDataSourceLocalImpl @Inject constructor(
        private val folderDao: FolderDao
): FolderDataSource {
    override fun getAll(account: Account): Single<List<String>>
            = folderDao.getAllByAccountId(account.id).map { it.map { e -> e.name } }
}