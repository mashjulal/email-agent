package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.Email
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import org.apache.commons.mail.util.MimeMessageParser
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.Store
import javax.mail.internet.MimeMessage
import javax.mail.search.SearchTerm
import kotlin.math.min

abstract class BaseMailRepository(
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain
) : MailRepository {

    protected companion object {

        const val SESSION_IMAP = 1
        const val SESSION_SMTP = 2

    }

    private val imapSession: Session = createSession(imapMailDomain)
    private val smtpSession: Session = createSession(smtpMailDomain)

    protected fun connectToStore(user: User, sessionType: Int): Store {
        lateinit var session: Session
        lateinit var host: String
        lateinit var protocol: String
        when(sessionType) {
            SESSION_IMAP -> {
                session = imapSession
                host = imapSession.getProperty("mail.imap.host")
                protocol = "imaps"
            }
            SESSION_SMTP -> {
                session = smtpSession
                host = smtpSession.getProperty("mail.smtp.host")
                protocol = "smtp"
            }
            else -> throw Exception("Unknown session type $sessionType")
        }

        val store = session.getStore(protocol)
        store.connect(host, user.address, user.password)
        return store
    }

    override fun getMail(user: User, folderName: String, offset: Int, count: Int): List<Email> {
        val store = connectToStore(user, SESSION_IMAP)
        val folder = store.getFolder(folderName)
        folder.open(Folder.READ_ONLY)
        val messages = folder.messages
                .reversedArray()
                .copyOfRange(offset*count, min(offset*count + count, folder.messageCount))
                .map { msg: Message ->
                    val msgParsed = MimeMessageParser(msg as MimeMessage).parse()
                    Email(msg, msgParsed)
                }
        folder.close()
        store.close()
        return messages
    }

    override fun getMailByNumber(user: User, folderName: String, number: Int): Email {
        val store = connectToStore(user, SESSION_IMAP)
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