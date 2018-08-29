package com.mashjulal.android.emailagent.ui.main

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.GetAccountsWithCurrentInteractor
import com.mashjulal.android.emailagent.domain.interactor.GetEmailHeadersInteractor
import com.mashjulal.android.emailagent.domain.interactor.GetFoldersInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.ControlledPullSubscriber
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val getAccountsWithCurrentInteractor: GetAccountsWithCurrentInteractor,
        private val getFoldersInteractor: GetFoldersInteractor,
        private val getEmailHeadersInteractor: GetEmailHeadersInteractor
): BasePresenter<MainView>() {

    private val initialPackCount: Long = 2
    private lateinit var currentUser: Account
    private lateinit var currentFolder: String
    private lateinit var emailHeaderSubscriber: ControlledPullSubscriber<List<EmailHeader>>

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
            emailHeaderSubscriber.requestMore(1)
            return
        }

        currentFolder = folder
        compositeDisposable.clear()
        if (this::emailHeaderSubscriber.isInitialized) {
            emailHeaderSubscriber.dispose()
        }
        emailHeaderSubscriber = ControlledPullSubscriber(
                { viewState.initMailList() },
                { viewState.updateMailList(it) },
                {},
                { viewState.stopUpdatingMailList() },
                initialPackCount
        )
        getEmailHeadersInteractor.getHeaders(currentUser, currentFolder, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(emailHeaderSubscriber)
    }

    fun onEmailClick(messageNumber: Int) {
        viewState.showMessageContent(currentUser, messageNumber)
    }

    fun requestNewEmail() {
        viewState.newEmail()
    }

    override fun onDestroy() {
        super.onDestroy()
        emailHeaderSubscriber.dispose()
        compositeDisposable.dispose()
    }

}