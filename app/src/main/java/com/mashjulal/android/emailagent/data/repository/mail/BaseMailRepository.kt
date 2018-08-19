package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.data.repository.mail.StoreUtils.SESSION_IMAP
import com.mashjulal.android.emailagent.domain.model.Email
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import org.apache.commons.mail.util.MimeMessageParser
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.search.SearchTerm
import kotlin.math.max

abstract class BaseMailRepository(
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain
) : MailRepository {

    private val imapSession: Session = StoreUtils.createSession(imapMailDomain)
    private val smtpSession: Session = StoreUtils.createSession(smtpMailDomain)

    override fun getMail(user: User, folderName: String, offset: Int, count: Int): List<Email> {
        val store = StoreUtils.connectToStore(user, imapSession, SESSION_IMAP)
        val folder = store.getFolder(folderName)
        folder.open(Folder.READ_ONLY)
        val msgCnt = folder.messageCount
        val messages = folder
                .getMessages(max(0, msgCnt-count*offset-count+1), msgCnt-count*offset)
                .map { msg: Message ->
                    val msgParsed = MimeMessageParser(msg as MimeMessage).parse()
                    Email(msg, msgParsed)
                }
                .reversed()
        folder.close()
        store.close()
        return messages
    }

    override fun getMailHeaders(user: User, folderName: String, offset: Int, count: Int): List<EmailHeader> {
        val store = StoreUtils.connectToStore(user, imapSession, SESSION_IMAP)
        val folder = store.getFolder(folderName)
        folder.open(Folder.READ_ONLY)
        val msgCnt = folder.messageCount
        val messages = folder
                .getMessages(max(0, msgCnt-count*offset-count+1), msgCnt-count*offset)
                .map { EmailHeader(it) }
                .reversed()
        folder.close()
        store.close()
        return messages
    }

    override fun getMailByNumber(user: User, folderName: String, number: Int): Email {
        val store = StoreUtils.connectToStore(user, imapSession, SESSION_IMAP)
        val folder = store.getFolder(folderName)
        folder.open(Folder.READ_ONLY)
        val term = object: SearchTerm() {
            override fun match(msg: Message?): Boolean
                    = msg!!.messageNumber == number
        }
        val msg = folder.search(term)[0]
        val msgParsed = MimeMessageParser(msg as MimeMessage).parse()
        val message = Email(msg, msgParsed)
        folder.close()
        store.close()
        return message
    }
}