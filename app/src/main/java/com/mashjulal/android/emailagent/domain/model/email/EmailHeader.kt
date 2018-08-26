package com.mashjulal.android.emailagent.domain.model.email

import java.util.*
import javax.mail.Flags
import javax.mail.Message
import javax.mail.internet.InternetAddress

data class EmailHeader(
        val messageNumber: Int,
        val subject: String,
        val from: EmailAddress,
        val receivedDate: Date,
        val isRead: Boolean
) {
    constructor(msg: Message): this(msg.messageNumber, msg.subject,
            EmailAddress(msg.from[0] as InternetAddress), msg.receivedDate, msg.isSet(Flags.Flag.SEEN))
}