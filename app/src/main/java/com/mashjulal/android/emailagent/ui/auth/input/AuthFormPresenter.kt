package com.mashjulal.android.emailagent.ui.auth.input

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
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
        val user = Account(0, "", email, pwd)
        mailDomainRepository.getByNameAndProtocol(
                getDomainFromEmail(user.address), Protocol.IMAP)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable { mailDomain -> StoreUtils.auth(mailDomain, user) }
                .andThen(accountRepository.addUser(user))
                .flatMapCompletable { userId -> preferenceManager.setLastSelectedUserId(userId) }
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