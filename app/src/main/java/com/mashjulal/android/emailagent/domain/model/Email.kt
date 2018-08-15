package com.mashjulal.android.emailagent.domain.model

import javax.mail.Flags
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.internet.InternetAddress

data class Email(
        val messageNumber: Int,
        val subject: String,
        val from: Address,
        val isRead: Boolean,
        val content: List<BodyPart>
) {
    constructor(message: Message): this(message.messageNumber, message.subject,
            Address(message.from[0] as InternetAddress), message.isSet(Flags.Flag.SEEN),
            extractContent(message)
    )
}

data class BodyPart(
        val mimeType: String,
        val content: String
)

data class Address(
        val email: String,
        val name: String
) {
    constructor(address: InternetAddress): this(address.address, address.personal ?: address.address)
}

private fun extractContent(message: Message): List<BodyPart> {
    val messageContent = message.content
    val parts = mutableListOf<BodyPart>()
    when (messageContent) {
        is Multipart -> {
            for (i in 0..(messageContent.count-1)) {
                val bodyPart = messageContent.getBodyPart(i)
                BodyPart(bodyPart.contentType, bodyPart.content.toString())
                if (bodyPart.isMimeType("text/html")) {
                    parts.add(BodyPart(bodyPart.contentType, bodyPart.content.toString()))
                }
            }
        }
        is String -> {
            parts.add(BodyPart("text/html", messageContent))
        }
    }
    return parts
}