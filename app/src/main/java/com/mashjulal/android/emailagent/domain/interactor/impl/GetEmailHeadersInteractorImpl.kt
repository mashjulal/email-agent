package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.GetEmailHeadersInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetEmailHeadersInteractorImpl @Inject constructor(
        private var emailRepositoryFactory: EmailRepositoryFactory
): GetEmailHeadersInteractor {

    override fun getHeaders(account: Account, folder: String, offset: Int): Flowable<List<EmailHeader>> {
        return emailRepositoryFactory.createRepository(account, folder)
                .subscribeOn(Schedulers.io())
                .flatMapPublisher { it.getMailHeaders() }
                .doOnError {
                    Timber.e(it)
                }
    }
}