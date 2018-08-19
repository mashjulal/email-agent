package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.FolderRepository
import javax.inject.Inject
import javax.mail.Session
import javax.mail.Store

class FolderRepositoryImpl @Inject constructor(
        imapMailDomain: MailDomain
): FolderRepository {

    private val imapSession: Session = createSession(imapMailDomain)

    override fun getAll(user: User): List<String> {
        val store = connectToStore(user)
        val folders = store.defaultFolder.list("*").map { it.name }
        store.close()
        return folders
    }

    private fun connectToStore(user: User): Store {
        val session = imapSession
        val host = imapSession.getProperty("mail.imap.host")
        val protocol = "imaps"
        val store = session.getStore(protocol)
        store.connect(host, user.address, user.password)
        return store
    }
}