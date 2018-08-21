package com.mashjulal.android.emailagent.ui.auth

import com.mashjulal.android.emailagent.data.repository.mail.StoreUtils
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.mail.AuthenticationFailedException

class AuthPresenter @Inject constructor(
        private val accountRepository: AccountRepository,
        private val mailDomainRepository: MailDomainRepository
): BasePresenter<AuthView>() {

    fun startAuth() {
        view?.showAuthForm()
    }

    fun tryToAuth(email: String, pwd: String) {
        Single.fromCallable {
            val user = User(0, "", email, pwd)
            val domain = user.address.substringAfter("@").substringBefore(".")
            val mailDomain = mailDomainRepository.getByName(domain).first { it.protocol == "imap" }
            mailDomain to user
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { (mailDomain, user) ->
                            StoreUtils.auth(mailDomain, user)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            { authSuccessful(user) },
                                            { authFailed(it) }
                                    )
                        },
                        { authFailed(it) }
                )
    }

    private fun authSuccessful(user: User) {
        val userId = accountRepository.addUser(user)
        view?.completeAuthorization(userId)
    }

    private fun authFailed(e: Throwable) {
        when (e) {
            is AuthenticationFailedException -> {
                view?.showError("Wrong email and/or password")
            }
            is NoSuchElementException -> {
                view?.showError("Unsupported mail domain")
            }
            else -> {
                view?.showError("Unexpected error: ${e.message}")
            }
        }
    }
}

interface AuthView: MvpView {
    fun showAuthForm()
    fun completeAuthorization(userId: Long)
    fun showError(error: String)
}