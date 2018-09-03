package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single

interface MailDomainDataStorage {
    fun add(mailDomain: MailDomain): Single<Long>
    fun getByNameAndProtocol(name: String, protocol: Protocol): Single<MailDomain>
}