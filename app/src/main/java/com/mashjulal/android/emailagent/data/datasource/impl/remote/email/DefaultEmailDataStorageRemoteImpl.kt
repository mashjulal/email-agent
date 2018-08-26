package com.mashjulal.android.emailagent.data.datasource.impl.remote.email

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Flowable
import io.reactivex.Maybe

class DefaultEmailDataStorageRemoteImpl (
        private val folder: String,
        imapMailDomain: MailDomain,
        smtpMailDomain: MailDomain
): BaseEmailDataStorageRemote(imapMailDomain, smtpMailDomain) {

    fun getMail(user: Account): Flowable<List<Email>> =
            super.getMail(user, folder)

    fun getMailHeaders(user: Account): Flowable<List<EmailHeader>> =
            super.getMailHeaders(user, folder)

    fun getMailByNumber(user: Account, mailNumber: Int): Maybe<Email> =
            super.getMailByNumber(user, folder, mailNumber)
}