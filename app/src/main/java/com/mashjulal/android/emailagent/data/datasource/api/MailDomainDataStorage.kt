package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Maybe

interface MailDomainDataStorage {
    fun getByNameAndProtocol(name: String, protocol: Protocol): Maybe<MailDomain>
}