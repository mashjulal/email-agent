package com.mashjulal.android.emailagent.data.repository.api

import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface MailRepository {
    fun getMail(): Flowable<List<Email>>
    fun getMailHeaders(): Flowable<List<EmailHeader>>
    fun getMailByNumber(number: Int): Single<Email>
    fun sendMail(email: Email): Completable
    fun search(query: String): Single<List<EmailHeader>>
}