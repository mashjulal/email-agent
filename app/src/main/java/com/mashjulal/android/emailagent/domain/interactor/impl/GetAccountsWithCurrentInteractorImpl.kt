package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.domain.interactor.GetAccountsWithCurrentInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetAccountsWithCurrentInteractorImpl @Inject constructor(
        private val preferenceManager: PreferenceManager,
        private val accountRepository: AccountRepository
        ): GetAccountsWithCurrentInteractor {

    override fun get(): Single<Triple<List<Account>, Account, Int>> {
        return accountRepository.getAll()
                .subscribeOn(Schedulers.io())
                .flatMap { users -> Single.fromCallable {
                    val lastUserId = preferenceManager.getLastSelectedUserId().blockingGet()
                    val user = users.find { it.id == lastUserId } ?: throw Exception()
                    Triple(users, user, users.indexOf(user))
                } }
                .doOnError {
                    Timber.e(it)
                }
    }
}