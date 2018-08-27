package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import io.reactivex.Completable

interface SendEmailInteractor {
    fun sendEmail(account: Account, to: String, subject: String,
                  text: String, subscription: String): Completable
}