package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.SearchEmailInteractor
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchEmailInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private var emailRepositoryFactory: EmailRepositoryFactory
): SearchEmailInteractor {

    override fun search(accountId: Long, folder: String, query: String): Single<List<EmailHeader>> {
        return accountRepository.getUserById(accountId)
                .subscribeOn(Schedulers.io())
                .flatMap { emailRepositoryFactory.createRepository(it, folder) }
                .flatMap { it.search(query) }
                .doOnError {
                    Timber.e(it)
                }
    }
}