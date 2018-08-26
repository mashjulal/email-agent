package com.mashjulal.android.emailagent.data.datasource.api

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface EmailDataSource {
    fun getMail(account: Account, folderName: String): Flowable<List<Email>>
    fun getMailHeaders(account: Account, folderName: String): Flowable<List<EmailHeader>>
    fun getMailByNumber(account: Account, folderName: String, number: Int): Maybe<Email>
    fun sendMail(account: Account, folderName: String, email: Email): Completable
}