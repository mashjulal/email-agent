package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.data.datasource.api.EmailDataSource
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import io.reactivex.Flowable
import io.reactivex.Maybe

class MailRepositoryImpl (
        private val account: Account,
        private val folderName: String,
        private val remoteMailDataSource: EmailDataSource
): MailRepository {
    override fun getMail(): Flowable<List<Email>>
            = remoteMailDataSource.getMail(account, folderName)

    override fun getMailHeaders(): Flowable<List<EmailHeader>>
            = remoteMailDataSource.getMailHeaders(account, folderName)

    override fun getMailByNumber(number: Int): Maybe<Email>
            = remoteMailDataSource.getMailByNumber(account, folderName, number)

}