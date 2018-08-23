package com.mashjulal.android.emailagent.ui.auth

import com.mashjulal.android.emailagent.data.repository.mail.StoreUtils
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteConstraintException
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
        val user = User(0, "", email, pwd)
        Single.fromCallable {
            val domain = user.address.substringAfter("@").substringBefore(".")
            val mailDomain = mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP)
            mailDomain
        }
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { mailDomain -> StoreUtils.auth(mailDomain, user) }
                .andThen(Single.fromCallable { accountRepository.addUser(user) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { userId: Long -> view?.completeAuthorization(userId) },
                        { e: Throwable -> authFailed(e) }
                )
    }

    private fun authFailed(e: Throwable) {
        when (e) {
            is AuthenticationFailedException -> {
                view?.showError("Wrong email and/or password")
            }
            is NoSuchElementException -> {
                view?.showError("Unsupported mail domain")
            }
            is SQLiteConstraintException -> {
                view?.showError("User with this email is already exist")
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