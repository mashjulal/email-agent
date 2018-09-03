package com.mashjulal.android.emailagent.data.repository.impl.mail

import com.mashjulal.android.emailagent.data.datasource.api.EmailDataSource
import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class MailRepositoryImpl (
        private val account: Account,
        private val folderName: String,
        private val remoteMailDataSource: EmailDataSource
): MailRepository {

    override fun getMail(): Flowable<List<Email>>
            = remoteMailDataSource.getMail(account, folderName)

    override fun getMailHeaders(): Flowable<List<EmailHeader>>
            = remoteMailDataSource.getMailHeaders(account, folderName)

    override fun getMailByNumber(number: Int): Single<Email>
            = remoteMailDataSource.getMailByNumber(account, folderName, number)

    override fun sendMail(email: Email): Completable
            = remoteMailDataSource.sendMail(account, folderName, email)

    override fun search(query: String): Single<List<EmailHeader>>
            = remoteMailDataSource.search(account, folderName, query)
}