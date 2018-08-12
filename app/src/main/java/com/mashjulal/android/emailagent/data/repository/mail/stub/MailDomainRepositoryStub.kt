package com.mashjulal.android.emailagent.data.repository.mail.stub

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository

class MailDomainRepositoryStub : MailDomainRepository {

    private val data: List<MailDomain> = listOf(
            MailDomain(1, "yandex", "imap", "imap.yandex.ru", 993),
            MailDomain(2, "yandex", "smtp", "smtp.yandex.ru", 465)
    )

    override fun getById(id: Long): MailDomain = data.find { it.id == id }!!

    override fun getByName(name: String): List<MailDomain> = data.filter { it.name == name }
}