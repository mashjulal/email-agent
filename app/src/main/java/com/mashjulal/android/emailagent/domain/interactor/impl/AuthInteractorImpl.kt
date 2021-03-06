package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.domain.interactor.AuthInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.utils.EmailUtils
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager,
        private val emailUtils: EmailUtils,
        private val storeUtils: StoreUtils
): AuthInteractor {
    override fun auth(account: Account): Completable {
        return mailDomainRepository.getByNameAndProtocol(
                emailUtils.getDomainFromEmail(account.address), Protocol.IMAP)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { mailDomain -> storeUtils.auth(mailDomain, account) }
                .andThen(accountRepository.addUser(account))
                .flatMapCompletable { userId ->
                    preferenceManager.setLastSelectedUserId(userId) }
                .doOnError { Timber.e(it) }
    }
}