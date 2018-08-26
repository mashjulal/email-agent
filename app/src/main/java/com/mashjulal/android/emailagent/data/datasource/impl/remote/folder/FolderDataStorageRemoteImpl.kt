package com.mashjulal.android.emailagent.data.datasource.impl.remote.folder

import com.mashjulal.android.emailagent.data.datasource.api.FolderDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Single
import javax.inject.Inject
import javax.mail.Session

class FolderDataStorageRemoteImpl @Inject constructor(
        mailDomain: MailDomain
): FolderDataSource {

    private val session: Session = StoreUtils.createSession(mailDomain)

    override fun getAll(account: Account): Single<List<String>> {
        return {
            val store = StoreUtils.connectToStore(account, session, StoreUtils.SESSION_IMAP)
            val folders = store.defaultFolder.list("*").map { it.name }
            store.close()
            folders
        }.toIoSingle()
    }
}