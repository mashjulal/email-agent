package com.mashjulal.android.emailagent.ui.auth.input

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.data.repository.mail.StoreUtils
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.sqlcipher.database.SQLiteConstraintException
import javax.inject.Inject
import javax.mail.AuthenticationFailedException

@InjectViewState
class AuthFormPresenter @Inject constructor(
        private val accountRepository: AccountRepository,
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager
): BasePresenter<AuthFormView>() {

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
                .flatMapCompletable { userId ->
                    Completable.fromAction { preferenceManager.setLastSelectedUserId(userId) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { viewState.completeAuthorization() },
                        { e: Throwable -> authFailed(e) }
                ).addToComposite(compositeDisposable)
    }

    private fun authFailed(e: Throwable) {
        val errorMessage = when (e) {
            is AuthenticationFailedException -> "Wrong email and/or password"
            is NoSuchElementException -> "Unsupported mail domain"
            is SQLiteConstraintException -> "User with this email is already exist"
            else -> "Unexpected error: ${e.message}"
        }
        viewState.showError(errorMessage)
    }
}