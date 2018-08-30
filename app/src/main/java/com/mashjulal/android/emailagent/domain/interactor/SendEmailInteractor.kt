package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.Email
import io.reactivex.Completable

interface SendEmailInteractor {
    fun sendEmail(account: Account, to: String, subject: String,
                  text: String, subscription: String): Completable
    fun sendEmail(account: Account, email: Email): Completable
}