package com.mashjulal.android.emailagent.ui.newemail

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailContent
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@InjectViewState
class NewEmailPresenter @Inject constructor(
        private val accountRepository: AccountRepository,
        private val emailRepositoryFactory: EmailRepositoryFactory
): BasePresenter<NewEmailView>() {

    fun onInit(accountId: Long) {
        accountRepository.getAll()
                .flatMap { accounts ->
                    Single.fromCallable {
                        val accountPos = accounts.indexOfFirst { it.id == accountId }
                        accounts to accountPos
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (accounts, pos) ->
                    viewState.setAccounts(accounts, pos)
                }
    }

    fun sendEmail(account: Account, to: String, subject: String, text: String, subscription: String) {
        val header = EmailHeader(0, subject, EmailAddress(account.address, account.name),
                EmailAddress(to, ""), Date(), false)
        val content = EmailContent("$text\n$subscription", "", emptyList())
        val email = Email(header, content)
        emailRepositoryFactory.createRepository(account, Folder.SENT.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    it.sendMail(email)
                }
                .doOnError {
                    Timber.e(it)
                }
                .subscribe {
                    viewState.close()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}