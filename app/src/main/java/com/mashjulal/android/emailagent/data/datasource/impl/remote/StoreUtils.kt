package com.mashjulal.android.emailagent.data.datasource.impl.remote

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Completable
import timber.log.Timber
import java.util.*
import javax.mail.Session
import javax.mail.Store


object StoreUtils {

    const val SESSION_IMAP = 1
    const val SESSION_SMTP = 2

    const val TIMEOUT = 20000

    fun createSession(mailDomain: MailDomain): Session {
        val protocol = mailDomain.protocol.name.toLowerCase()
        val properties = Properties()
        properties["mail.$protocol.host"] = mailDomain.host
        properties["mail.$protocol.port"] = mailDomain.port.toString()
        properties["mail.$protocol.auth"] = mailDomain.needAuth
        properties["mail.$protocol.connectiontimeout"] = TIMEOUT.toString()
        properties["mail.$protocol.timeout"] = TIMEOUT.toString()
        if (mailDomain.protocol == Protocol.SMTP) {
            properties["mail.$protocol.starttls.enable"] = "true"
        }
        return Session.getInstance(properties)
    }

    fun connectToStore(user: Account, session: Session, sessionType: Int): Store {
        lateinit var host: String
        lateinit var protocol: String
        when(sessionType) {
            SESSION_IMAP -> {
                host = session.getProperty("mail.imap.host")
                protocol = "imaps"
            }
            SESSION_SMTP -> {
                host = session.getProperty("mail.smtp.host")
                protocol = "smtps"
            }
            else -> throw Exception("Unknown session type $sessionType")
        }

        val store = session.getStore(protocol)
        store.connect(host, user.address, user.password)
        return store
    }

    fun auth(mailDomain: MailDomain, user: Account): Completable =
            Completable.fromAction {
                val session = createSession(mailDomain)
                connectToStore(user, session, SESSION_IMAP)
            }.doOnError {
                Timber.e(it)
            }
}