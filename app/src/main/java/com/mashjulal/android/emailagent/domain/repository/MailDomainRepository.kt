package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Maybe

interface MailDomainRepository {
    fun getByNameAndProtocol(name: String, protocol: Protocol): Maybe<MailDomain>
}