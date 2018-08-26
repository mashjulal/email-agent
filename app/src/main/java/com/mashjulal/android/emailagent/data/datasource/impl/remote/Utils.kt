package com.mashjulal.android.emailagent.data.datasource.impl.remote

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import io.reactivex.Completable
import java.util.*
import javax.mail.Session
import javax.mail.Store


object StoreUtils {

    const val SESSION_IMAP = 1
    const val SESSION_SMTP = 2

    fun createSession(mailDomain: MailDomain): Session {
        val protocol = mailDomain.protocol.name.toLowerCase()
        val properties = Properties()
        properties["mail.$protocol.host"] = mailDomain.host
        properties["mail.$protocol.port"] = mailDomain.port
        properties["mail.$protocol.auth"] = mailDomain.needAuth
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
                protocol = "smtp"
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
            }
}