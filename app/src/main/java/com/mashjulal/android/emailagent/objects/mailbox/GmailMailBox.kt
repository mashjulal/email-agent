package com.mashjulal.android.emailagent.objects.mailbox

import com.mashjulal.android.emailagent.objects.user.User
import com.sun.mail.imap.IMAPFolder
import java.util.*
import javax.mail.Folder
import javax.mail.Session
import javax.mail.Store
import kotlin.NoSuchElementException

class GmailMailBox(
        private val user: User
): MailBox {

    companion object {

        private const val FOLDER_INBOX = "INBOX"
        private const val FOLDER_ALL = "*"

        private const val ATTRIBUTE_SENT = "\\Sent"
        private const val ATTRIBUTE_SPAM = "\\Junk"
        private const val ATTRIBUTE_TRASH = "\\Trash"
        private const val ATTRIBUTE_DRAFTS = "\\Drafts"

        private val imapProperties = Properties()
        init {
            imapProperties["mail.imap.host"] = "imap.gmail.com"
            imapProperties["mail.imap.port"] = "993"
        }
        private val smtpProperties = Properties()
        init {
            smtpProperties["mail.smtp.host"] = "smtp.gmail.com"
            smtpProperties["mail.smtp.socketFactory.port"] = "465"
            smtpProperties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            smtpProperties["mail.smtp.auth"] = "true"
            smtpProperties["mail.smtp.port"] = "465"
        }

        private val imapSession: Session = Session.getDefaultInstance(imapProperties)
        private val smtpSession: Session = Session.getDefaultInstance(smtpProperties)
    }

    override fun getInboxFolder(): Folder {
        val store = connectToStore("imaps", imapProperties["mail.imap.host"].toString())
        val folder = store.getFolder(FOLDER_INBOX)
        return folder
    }

    override fun getSentFolder(): Folder {
        return findStoreWithAttribute(ATTRIBUTE_SENT) ?: throw NoSuchElementException()
    }

    override fun getTrashFolder(): Folder {
        return findStoreWithAttribute(ATTRIBUTE_TRASH) ?: throw NoSuchElementException()
    }

    override fun getSpamFolder(): Folder {
        return findStoreWithAttribute(ATTRIBUTE_SPAM) ?: throw NoSuchElementException()
    }

    override fun getDraftsFolder(): Folder {
        return findStoreWithAttribute(ATTRIBUTE_DRAFTS) ?: throw NoSuchElementException()
    }

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

    private fun findStoreWithAttribute(attribute: String): Folder? {
        val store = connectToStore("imaps", imapProperties["mail.imap.host"].toString())
        val folders = store.defaultFolder.list(FOLDER_ALL)
        for (folder in folders) {
            val imapFolder = folder as IMAPFolder
            if (imapFolder.attributes.contains(attribute)) {
                return folder
            }
        }
        return null
    }
}