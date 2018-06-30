package com.mashjulal.android.emailagent.objects.mailbox

import com.mashjulal.android.emailagent.objects.user.User
import java.util.*
import javax.mail.Folder
import javax.mail.Session
import javax.mail.Store

class YandexMailBox(
        private val user: User
): MailBox {

    companion object {

        private const val FOLDER_INBOX = "INBOX"
        private const val FOLDER_DRAFTS = "DRAFTS"
        private const val FOLDER_SENT = "SENT"
        private const val FOLDER_SPAM = "SPAM"
        private const val FOLDER_TRASH = "TRASH"
        private const val FOLDER_ALL = "*"

        private val imapProperties = Properties()
        init {
            imapProperties["mail.imap.host"] = "imap.yandex.ru"
            imapProperties["mail.imap.port"] = "993"
        }
        private val smtpProperties = Properties()
        init {
            smtpProperties["mail.smtp.host"] = "smtp.yandex.ru"
            smtpProperties["mail.smtp.socketFactory.port"] = "465"
            smtpProperties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            smtpProperties["mail.smtp.auth"] = "true"
            smtpProperties["mail.smtp.port"] = "465"
        }

        private val imapSession: Session = Session.getDefaultInstance(imapProperties)
        private val smtpSession: Session = Session.getDefaultInstance(smtpProperties)
    }

    override fun getInboxFolder(): Folder = getFolder(FOLDER_INBOX)

    override fun getSentFolder(): Folder = getFolder(FOLDER_SENT)

    override fun getTrashFolder(): Folder = getFolder(FOLDER_TRASH)

    override fun getSpamFolder(): Folder = getFolder(FOLDER_SPAM)

    override fun getDraftsFolder(): Folder = getFolder(FOLDER_DRAFTS)

    override fun getFolder(folderName: String): Folder {
        val store = connectToStore("imaps", imapProperties["mail.imap.host"].toString())
        return store.getFolder(folderName)
    }

    override fun getAllFolders(): List<Folder> {
        val store = connectToStore("imaps", imapProperties["mail.imap.host"].toString())
        val folders = store.defaultFolder.list()
        return folders.toList()
    }

    private fun connectToStore(protocol: String, host: String): Store {
        val store = imapSession.getStore(protocol)
        store.connect(host, user.address, user.password)
        return store
    }
}