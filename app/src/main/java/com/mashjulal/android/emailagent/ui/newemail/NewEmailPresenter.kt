package com.mashjulal.android.emailagent.ui.newemail

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.GetAccountsWithCurrentInteractor
import com.mashjulal.android.emailagent.domain.interactor.SendEmailInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class NewEmailPresenter @Inject constructor(
        private val getAccountsWithCurrentInteractor: GetAccountsWithCurrentInteractor,
        private val sendEmailInteractor: SendEmailInteractor
): BasePresenter<NewEmailView>() {

    fun onInit() {
        getAccountsWithCurrentInteractor.get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (accounts, _, pos) ->
                    viewState.setAccounts(accounts, pos)
                }
    }

    fun sendEmail(account: Account, to: String, subject: String, text: String, subscription: String) {
        sendEmailInteractor.sendEmail(account, to, subject, text, subscription)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewState.close()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}