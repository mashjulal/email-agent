package com.mashjulal.android.emailagent.ui.main

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.GetAccountsWithCurrentInteractor
import com.mashjulal.android.emailagent.domain.interactor.GetEmailHeadersInteractor
import com.mashjulal.android.emailagent.domain.interactor.GetFoldersInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import org.reactivestreams.Subscription
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val getAccountsWithCurrentInteractor: GetAccountsWithCurrentInteractor,
        private val getFoldersInteractor: GetFoldersInteractor,
        private val getEmailHeadersInteractor: GetEmailHeadersInteractor
): BasePresenter<MainView>() {

    private lateinit var currentUser: Account
    private lateinit var currentFolder: String
    private var emailSubscription: Subscription? = null

    override fun onFirstViewAttach() {
        onInit()
    }

    private fun onInit() {
        getAccountsWithCurrentInteractor.get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (users: List<Account>, user: Account, pos: Int) ->
                    currentUser = user
                    viewState.updateUserList(users)
                    viewState.setCurrentUser(user, pos)
                    requestFolderList(user)
                }
                .addToComposite(compositeDisposable)
    }

    fun requestFolderList(account: Account) {
        compositeDisposable.clear()
        currentUser = account
        getFoldersInteractor.getFoldersWithForNewAccount(currentUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (folders: List<String>, fldr: String, pos: Int) ->
                    viewState.updateFolderList(folders)
                    currentFolder = fldr
                    viewState.setCurrentFolder(currentFolder, pos)
                    requestUpdateMailList(offset = 0)
                }
                .addToComposite(compositeDisposable)
    }

    fun requestUpdateMailList(folder: String = currentFolder, offset: Int) {
        if (offset > 0) {
            emailSubscription?.request(1)
            return
        }

        currentFolder = folder
        compositeDisposable.clear()
        emailSubscription?.cancel()
        emailSubscription = null
        getEmailHeadersInteractor.getHeaders(currentUser, currentFolder, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    emailSubscription = it
                    viewState.initMailList()
                }
                .doOnNext {
                    viewState.updateMailList(it)
                }
                .doOnComplete {
                    viewState.stopUpdatingMailList()
                }
                .subscribe()
                .addToComposite(compositeDisposable)
    }

    fun onEmailClick(messageNumber: Int) {
        viewState.showMessageContent(currentUser, messageNumber)
    }

    fun requestNewEmail() {
        viewState.newEmail()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}