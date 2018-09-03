package com.mashjulal.android.emailagent.domain.interactor

import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Single

interface SearchEmailInteractor {
    fun search(accountId: Long, folder: String, query: String): Single<List<EmailHeader>>
}