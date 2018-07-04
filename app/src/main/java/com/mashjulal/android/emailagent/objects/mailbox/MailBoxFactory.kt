package com.mashjulal.android.emailagent.objects.mailbox

import com.mashjulal.android.emailagent.objects.user.User
import java.lang.IllegalArgumentException

interface MailBoxFactory {
    fun createFromUser(user: User): MailBox
}

class StandardMailboxFactory {

    companion object : MailBoxFactory {
        const val DOMAIN_GMAIL = "gmail.com"
        const val DOMAIN_YANDEX = "yandex.ru"

        override fun createFromUser(user: User): MailBox {
            val domain = getEmailDomain(user.address)
            return when (domain) {
                DOMAIN_GMAIL -> GmailMailBox(user)
                DOMAIN_YANDEX -> YandexMailBox(user)
                else -> throw IllegalArgumentException("Unknown email domain $domain")
            }
        }

        private fun getEmailDomain(emailDomain: String) = emailDomain.split("@")[1]
    }
}

