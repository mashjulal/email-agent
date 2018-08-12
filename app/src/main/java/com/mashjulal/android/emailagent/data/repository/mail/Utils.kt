package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.MailDomain
import java.util.*
import javax.mail.Session

fun createSession(mailDomain: MailDomain): Session {
    val protocol = mailDomain.protocol
    val properties = Properties()
    properties["mail.$protocol.host"] = mailDomain.host
    properties["mail.$protocol.port"] = mailDomain.port
    properties["mail.$protocol.auth"] = mailDomain.needAuth
    return Session.getInstance(properties)
}