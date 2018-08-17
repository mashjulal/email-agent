package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.Email
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.User

private const val PAGE_SIZE = 10

class DefaultMailRepository(
        private val folder: String,
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain
): BaseMailRepository(imapMailDomain, smtpMailDomain) {

    fun getMail(user: User, offset: Int): List<Email> =
            super.getMail(user, folder, offset, PAGE_SIZE)

    fun getMailHeaders(user: User, offset: Int) =
            super.getMailHeaders(user, folder, offset, PAGE_SIZE)

    fun getMailByNumber(user: User, mailNumber: Int): Email =
            super.getMailByNumber(user, folder, mailNumber)

    companion object {
        const val FOLDER_INBOX = "INBOX"
        const val FOLDER_DRAFTS = "DRAFTS"
        const val FOLDER_SENT = "SENT"
        const val FOLDER_SPAM = "SPAM"
        const val FOLDER_TRASH = "TRASH"
    }
}