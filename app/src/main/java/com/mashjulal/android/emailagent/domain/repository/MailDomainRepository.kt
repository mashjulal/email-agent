package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol

interface MailDomainRepository {
    fun getByNameAndProtocol(name: String, protocol: Protocol): MailDomain
}