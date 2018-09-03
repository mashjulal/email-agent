package com.mashjulal.android.emailagent.ui.auth.input

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.AuthInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import net.sqlcipher.database.SQLiteConstraintException
import javax.inject.Inject
import javax.mail.AuthenticationFailedException

@InjectViewState
class AuthFormPresenter @Inject constructor(
        private val authInteractor: AuthInteractor
): BasePresenter<AuthFormView>() {

    fun tryToAuth(name: String, email: String, pwd: String) {
        authInteractor.auth(Account(0, name, email, pwd))
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