package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.GetEmailContentInteractor
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetEmailContentInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val emailRepositoryFactory: EmailRepositoryFactory
): GetEmailContentInteractor {

    override fun getContent(accountId: Long, folder: String, messageNumber: Int): Maybe<Email> {
        return accountRepository.getUserById(accountId)
                .flatMap { emailRepositoryFactory.createRepository(it, folder) }
                .flatMap {it.getMailByNumber(messageNumber) }
                .subscribeOn(Schedulers.computation())
                .doOnError {
                    Timber.e(it)
                }
    }
}