package com.mashjulal.android.emailagent.ui.search

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.GetAccountsWithCurrentInteractor
import com.mashjulal.android.emailagent.domain.interactor.SearchEmailInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
        private val searchEmailInteractor: SearchEmailInteractor,
        private val getAccountsWithCurrentInteractor: GetAccountsWithCurrentInteractor
): BasePresenter<SearchView>() {

    private lateinit var accounts: List<Account>
    private lateinit var currentUser: Account

    fun onInit(accountId: Long, folder: String, query: String) {
        getAccountsWithCurrentInteractor.get(accountId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { (users: List<Account>, user: Account, pos: Int) ->
                            accounts = users
                            viewState.updateUserList(users)
                            viewState.setCurrentUser(user, pos)
                            searchEmailInteractor.search(user.id, folder, query)
                        },
                        { viewState.showError(it.message ?: "") }
                ).addToComposite(compositeDisposable)
    }

    fun search(accountId: Long, folder: String, query: String) {
        { currentUser = accounts.find { it.id == accountId } ?: throw Exception() }
                .toIoSingle()
                .flatMap { searchEmailInteractor.search(currentUser.id, folder, query) }
                .subscribe(
                        { viewState.showResults(it) },
                        { viewState.showError(it.message ?: "") }
                ).addToComposite(compositeDisposable)
    }

    fun onEmailClick(messageNumber: Int) {
        viewState.showMessageContent(currentUser, messageNumber)
    }
}