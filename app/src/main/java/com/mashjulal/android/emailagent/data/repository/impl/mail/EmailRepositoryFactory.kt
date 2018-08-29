package com.mashjulal.android.emailagent.data.repository.impl.mail

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import io.reactivex.Maybe

interface EmailRepositoryFactory {
    fun createRepository(account: Account, folder: String): Maybe<MailRepository>
}