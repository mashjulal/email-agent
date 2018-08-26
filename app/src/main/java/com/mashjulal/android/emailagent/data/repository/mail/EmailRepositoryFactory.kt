package com.mashjulal.android.emailagent.data.repository.mail

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import io.reactivex.Maybe

interface EmailRepositoryFactory {
    fun createRepository(account: Account, folder: String): Maybe<MailRepository>
}