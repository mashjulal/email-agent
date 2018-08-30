package com.mashjulal.android.emailagent.domain.interactor.impl

import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.SendEmailInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailContent
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SendEmailInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val emailRepositoryFactory: EmailRepositoryFactory
): SendEmailInteractor {

    override fun sendEmail(account: Account, to: String, subject: String, text: String, subscription: String): Completable {
        val header = EmailHeader(0, subject,
                EmailAddress(account.address, account.name),
                EmailAddress(to, ""), Date(), false)
        val content = EmailContent("$text\n$subscription",
                "", emptyList())
        return sendEmail(account, Email(header, content))
    }

    override fun sendEmail(account: Account, email: Email): Completable {
        return emailRepositoryFactory.createRepository(account, Folder.SENT.name)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { rep ->
                    rep.sendMail(email)
                }
                .doOnError {
                    Timber.e(it)
                }
    }
}