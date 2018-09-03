package com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain

import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single
import javax.inject.Inject

class EmailDomainDataSourceLocalImpl @Inject constructor(
        private val emailDomainDao: EmailDomainDao
): MailDomainDataStorage {

    override fun add(mailDomain: MailDomain): Single<Long> {
        return emailDomainDao.insert(toEntity(mailDomain))
    }

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Single<MailDomain> {
        return emailDomainDao.getByNameAndProtocol(name, protocol).map { toModel(it) }
    }

    private fun toModel(entity: EmailDomainEntity): MailDomain =
            MailDomain(entity.id, entity.name, entity.protocol, entity.host, entity.port, entity.needAuth)

    private fun toEntity(model: MailDomain): EmailDomainEntity =
            EmailDomainEntity(model.id, model.name, model.host, model.port, model.protocol, model.needAuth)
}