package com.mashjulal.android.emailagent.data.datasource.impl.local.db

import android.webkit.MimeTypeMap
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAddressEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAttachmentEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainEntity
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.email.*
import java.io.File
import java.util.*

object EmailDomainMappers {

    fun toEmailDomainModel(entity: EmailDomainEntity): MailDomain =
            MailDomain(entity.id, entity.name, entity.protocol, entity.host, entity.port, entity.needAuth)

    fun toEmailDomainEntity(model: MailDomain): EmailDomainEntity =
            EmailDomainEntity(model.id, model.name, model.host, model.port, model.protocol, model.needAuth)
}

object AccountMappers {

    fun toAccountModel(entity: AccountEntity): Account =
            Account(entity.id, entity.name, entity.email, entity.pwd)

    fun toAccountEntity(model: Account): AccountEntity =
            AccountEntity(model.id, model.name, model.address, model.password)
}

object EmailMappers {

    fun toEmailModel(entity: EmailEntity, from: EmailAddressEntity,
                     to: EmailAddressEntity, attachments: List<EmailAttachmentEntity>): Email {
        val header = EmailHeader(entity.messageNumber, entity.subject,
                EmailAddress(from.email, from.name),
                EmailAddress(to.email, to.name),
                Date(entity.receivedDate), entity.isRead)
        val attch = attachments.map { att ->
            val ext = MimeTypeMap.getFileExtensionFromUrl(att.path)
            val contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            EmailAttachment(att.path, contentType, File(att.path).inputStream())
        }
        val content = EmailContent(entity.textContent, entity.htmlContent, attch)
        return Email(header, content)
    }
}