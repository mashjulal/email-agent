package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.FolderRepository
import javax.inject.Inject
import javax.mail.Session

class FolderRepositoryImpl @Inject constructor(
        imapMailDomain: MailDomain
): FolderRepository {

    private val imapSession: Session = StoreUtils.createSession(imapMailDomain)

    override fun getAll(user: User): List<String> {
        val store = StoreUtils.connectToStore(user, imapSession, StoreUtils.SESSION_IMAP)
        val folders = store.defaultFolder.list("*").map { it.name }
        store.close()
        return folders
    }
}