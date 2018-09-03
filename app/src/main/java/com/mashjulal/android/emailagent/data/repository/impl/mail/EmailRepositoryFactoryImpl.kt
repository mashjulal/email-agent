package com.mashjulal.android.emailagent.data.repository.impl.mail

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.datasource.impl.remote.email.DefaultEmailDataStorageRemoteImpl
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.utils.EmailUtils
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Single
import javax.inject.Inject

class EmailRepositoryFactoryImpl @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val emailUtils: EmailUtils,
        private val storeUtils: StoreUtils
): EmailRepositoryFactory {
    override fun createRepository(account: Account, folder: String): Single<MailRepository> {
        return {
            val domain = emailUtils.getDomainFromEmail(account.address)

            val mailRep = DefaultEmailDataStorageRemoteImpl(
                    folder,
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP).blockingGet(),
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.SMTP).blockingGet(),
                    storeUtils
            )
            MailRepositoryImpl(account, folder, mailRep)
        }.toIoSingle()
    }
}