package com.mashjulal.android.emailagent.data.datasource.impl.remote.email

import com.mashjulal.android.emailagent.data.datasource.api.EmailDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils.SESSION_IMAP
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Completable
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.apache.commons.mail.util.MimeMessageParser
import java.util.concurrent.Callable
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import javax.mail.search.BodyTerm
import javax.mail.search.OrTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm
import kotlin.math.max

abstract class BaseEmailDataStorageRemote (
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain,
        private val storeUtils: StoreUtils
) : EmailDataSource {

    private val imapSession: Session = storeUtils.createSession(imapMailDomain)
    private val smtpSession: Session = storeUtils.createSession(smtpMailDomain)

    private val PAGE_SIZE = 10

    override fun getMail(account: Account, folderName: String): Flowable<List<Email>> {
        var offset = 0
        return Flowable.generate(
                Callable<List<Email>>{ requestMail(account, folderName, offset) },
                BiFunction<List<Email>, Emitter<List<Email>>, List<Email>> { emails, emitter ->
                    if (emails.isNotEmpty()) {
                        emitter.onNext(emails)
                        offset++
                        return@BiFunction requestMail(account, folderName, offset)
                    } else {
                        emitter.onComplete()
                        return@BiFunction emptyList()
                    }
                }
        ).subscribeOn(Schedulers.io())
    }

    override fun getMailHeaders(account: Account, folderName: String): Flowable<List<EmailHeader>> {
        var offset = 0
        return Flowable.generate(
                Callable<List<EmailHeader>>{ requestMailHeaders(account, folderName, offset) },
                BiFunction<List<EmailHeader>, Emitter<List<EmailHeader>>, List<EmailHeader>> {
                    emails, emitter ->
                    if (emails.size == PAGE_SIZE) {
                        emitter.onNext(emails)
                        offset++
                        return@BiFunction requestMailHeaders(account, folderName, offset)
                    } else {
                        emitter.onComplete()
                        return@BiFunction emptyList()
                    }
                }
        ).subscribeOn(Schedulers.io())
    }

    private fun requestMailHeaders(account: Account, folderName: String, offset: Int): List<EmailHeader> {
        return requestMail(account, folderName, offset) { EmailHeader(it) }
    }

    private fun requestMail(account: Account, folderName: String, offset: Int): List<Email> {
        return requestMail(account, folderName, offset) {
            val msgParsed = MimeMessageParser(it as MimeMessage).parse()
            Email(it, msgParsed)
        }
    }

    private fun <T> requestMail(account: Account, folderName: String, offset: Int,
                                mapper: (Message) -> T): List<T> {
        var store: Store? = null
        var folder: Folder? = null
        try {
            store = storeUtils.connectToStore(account, imapSession, SESSION_IMAP)
            folder = store.getFolder(folderName)
            folder.open(Folder.READ_ONLY)
            val msgCnt = folder.messageCount
            val start = max(1, msgCnt-PAGE_SIZE*offset-PAGE_SIZE+1)
            val end = msgCnt-PAGE_SIZE*offset
            return folder
                    .getMessages(start, end)
                    .map { mapper.invoke(it) }
                    .reversed()
        } finally {
            folder?.close()
            store?.close()
        }
    }

    override fun getMailByNumber(account: Account, folderName: String, number: Int): Single<Email> {
        return Single.fromCallable {
            val store = storeUtils.connectToStore(account, imapSession, SESSION_IMAP)
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
            message
        }.subscribeOn(Schedulers.io())

    }

    override fun sendMail(account: Account, folderName: String, email: Email): Completable {
        return Completable.fromCallable {
            if (email.emailHeader.to == null) throw Exception("Null recipient address")

            val session = Session.getInstance(smtpSession.properties, object: Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(account.address, account.password)
                }
            })
            val transport = session.getTransport(Protocol.SMTP.name.toLowerCase())
            transport.connect()
            transport.use {
                val msg = MimeMessage(session)
                msg.setFrom(InternetAddress(account.address))
                msg.addRecipient(Message.RecipientType.TO, InternetAddress(email.emailHeader.to.email))
                msg.subject = email.emailHeader.subject
                msg.setText(email.content.textContent)
                it.sendMessage(msg, msg.allRecipients)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun search(account: Account, folder: String, query: String): Single<List<EmailHeader>> {
        return {
            val store = storeUtils.connectToStore(account, imapSession, SESSION_IMAP)
            val fldr = store.getFolder(folder)
            fldr.open(Folder.READ_ONLY)
            val searchTerm = OrTerm(SubjectTerm(query), BodyTerm(query))
            val messages = fldr
                    .search(searchTerm)
                    .map { EmailHeader(it) }
                    .reversed()
            fldr.close()
            store.close()
            messages
        }.toIoSingle()
    }
}