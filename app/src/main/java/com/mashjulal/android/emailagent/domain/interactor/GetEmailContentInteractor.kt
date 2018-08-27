package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.email.Email
import io.reactivex.Maybe


interface GetEmailContentInteractor {
    fun getContent(accountId: Long, folder: String, messageNumber: Int): Maybe<Email>
}