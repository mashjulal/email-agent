package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.MailDomain

interface MailDomainRepository {
    fun getById(id: Long): MailDomain
    fun getByName(name: String): List<MailDomain>
}