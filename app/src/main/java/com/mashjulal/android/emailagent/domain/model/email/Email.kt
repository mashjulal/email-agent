package com.mashjulal.android.emailagent.domain.model.email

import org.apache.commons.mail.util.MimeMessageParser
import javax.mail.Message

data class Email(
        val emailHeader: EmailHeader,
        val content: EmailContent
) {
    constructor(msg: Message, msgParsed: MimeMessageParser):
            this(EmailHeader(msg), EmailContent(
                    msgParsed.plainContent ?: "",
                    msgParsed.htmlContent ?: "",
                    msgParsed.attachmentList.map { EmailAttachment(it.name, it.contentType, it.inputStream) }))
}