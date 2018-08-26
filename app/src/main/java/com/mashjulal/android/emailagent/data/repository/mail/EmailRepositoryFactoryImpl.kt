package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.data.datasource.impl.remote.email.DefaultEmailDataStorageRemoteImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
import com.mashjulal.android.emailagent.utils.toIoMaybe
import io.reactivex.Maybe
import javax.inject.Inject

class EmailRepositoryFactoryImpl @Inject constructor(
        private val mailDomainRepository: MailDomainRepository
): EmailRepositoryFactory {
    override fun createRepository(account: Account, folder: String): Maybe<MailRepository> {
        return {
            val domain = getDomainFromEmail(account.address)

            val mailRep = DefaultEmailDataStorageRemoteImpl(
                    folder,
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP).blockingGet(),
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.SMTP).blockingGet()
            )
            MailRepositoryImpl(account, folder, mailRep)
        }.toIoMaybe()
    }
}