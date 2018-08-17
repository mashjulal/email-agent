package com.mashjulal.android.emailagent.domain.model

import org.apache.commons.mail.util.MimeMessageParser
import java.io.InputStream
import javax.mail.Flags
import javax.mail.Message
import javax.mail.internet.InternetAddress

data class Email(
        val emailHeader: EmailHeader,
        val content: EmailContent
) {
    constructor(msg: Message, msgParsed: MimeMessageParser):
            this(EmailHeader(msg), EmailContent(
                    msgParsed.htmlContent,
                    msgParsed.attachmentList.map { Attachment(it.name, it.contentType, it.inputStream) }))
}

data class EmailHeader(
        val messageNumber: Int,
        val subject: String,
        val from: Address,
        val isRead: Boolean
) {
    constructor(msg: Message): this(msg.messageNumber, msg.subject,
            Address(msg.from[0] as InternetAddress), msg.isSet(Flags.Flag.SEEN))
}

data class EmailContent(
        val htmlContent: String,
        val attachments: List<Attachment>
)

data class Attachment(
        val filename: String,
        val contentType: String,
        val inputStream: InputStream
)

data class Address(
        val email: String,
        val name: String
) {
    constructor(address: InternetAddress): this(address.address, address.personal ?: address.address)
}