package com.mashjulal.android.emailagent.data.repository.impl.folder

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.datasource.impl.remote.folder.FolderDataStorageRemoteImpl
import com.mashjulal.android.emailagent.data.repository.api.FolderRepository
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.utils.EmailUtils
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Single
import javax.inject.Inject

class FolderRepositoryFactoryImpl @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val emailUtils: EmailUtils,
        private val storeUtils: StoreUtils
): FolderRepositoryFactory {
    override fun createFolderRepository(account: Account): Single<FolderRepository> {
        return {
            val domain = emailUtils.getDomainFromEmail(account.address)
            val mailDomain = mailDomainRepository
                    .getByNameAndProtocol(domain, Protocol.IMAP).blockingGet()
            val folderDataSource = FolderDataStorageRemoteImpl(mailDomain, storeUtils)
            FolderRepositoryImpl(folderDataSource)
        }.toIoSingle()
    }
}