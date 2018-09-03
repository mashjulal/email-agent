package com.mashjulal.android.emailagent.data.datasource.impl.remote.email

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Flowable
import io.reactivex.Single

class DefaultEmailDataStorageRemoteImpl (
        private val folder: String,
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain,
        storeUtils: StoreUtils
): BaseEmailDataStorageRemote(imapMailDomain, smtpMailDomain, storeUtils) {

    fun getMail(user: Account): Flowable<List<Email>> =
            super.getMail(user, folder)

    fun getMailHeaders(user: Account): Flowable<List<EmailHeader>> =
            super.getMailHeaders(user, folder)

    fun getMailByNumber(user: Account, mailNumber: Int): Single<Email> =
            super.getMailByNumber(user, folder, mailNumber)

    fun search(user: Account, query: String): Single<List<EmailHeader>> =
            super.search(user, folder, query)
}