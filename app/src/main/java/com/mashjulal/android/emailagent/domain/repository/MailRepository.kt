package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface MailRepository {
    fun getMail(): Flowable<List<Email>>
    fun getMailHeaders(): Flowable<List<EmailHeader>>
    fun getMailByNumber(number: Int): Maybe<Email>
    fun sendMail(email: Email): Completable
}