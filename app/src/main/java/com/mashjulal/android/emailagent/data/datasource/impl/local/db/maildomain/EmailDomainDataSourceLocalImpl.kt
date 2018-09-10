package com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain

import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.EmailDomainMappers
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single
import javax.inject.Inject

class EmailDomainDataSourceLocalImpl @Inject constructor(
        private val emailDomainDao: EmailDomainDao
): MailDomainDataStorage {

    override fun add(mailDomain: MailDomain): Single<Long> {
        return emailDomainDao.insert(EmailDomainMappers.toEmailDomainEntity(mailDomain))
    }

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Single<MailDomain> {
        return emailDomainDao.getByNameAndProtocol(name, protocol)
                .map { EmailDomainMappers.toEmailDomainModel(it) }
    }
}