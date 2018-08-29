package com.mashjulal.android.emailagent.data.repository.impl.maildomain

import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import io.reactivex.Maybe
import javax.inject.Inject

class MailDomainRepositoryImpl @Inject constructor(
        private val mailDomainDataStorage: MailDomainDataStorage
) : MailDomainRepository {

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Maybe<MailDomain>
        = mailDomainDataStorage.getByNameAndProtocol(name, protocol)
}