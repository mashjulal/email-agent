package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Flowable

interface GetEmailHeadersInteractor {
    fun getHeaders(account: Account, folder: String, offset: Int): Flowable<List<EmailHeader>>
}