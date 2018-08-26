package com.mashjulal.android.emailagent.ui.main

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.datasource.impl.remote.folder.FolderDataStorageRemoteImpl
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.MailRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val resources: Resources,
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager,
        private val accountRepository: AccountRepository,
        private var emailRepositoryFactory: EmailRepositoryFactory
): BasePresenter<MainView>() {

    private lateinit var users: List<Account>
    private lateinit var currentUser: Account
    private var currentFolder: String = Folder.INBOX.name
    private var emailSubscription: Subscription? = null
    private lateinit var emailRepository: MailRepository

    override fun onFirstViewAttach() {
        onInit()
    }

    private fun onInit() {
        accountRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { users -> Single.fromCallable {
                    val lastUserId = preferenceManager.getLastSelectedUserId().blockingGet()
                    val user = users.find { it.id == lastUserId } ?: throw Exception()
                    Triple(users, user, users.indexOf(user))
                } }
                .subscribe { (users: List<Account>, user: Account, pos: Int) ->
                    this.users = users
                    currentUser = user
                    viewState.updateUserList(users)
                    viewState.setCurrentUser(user, pos)
                    requestFolderList(currentFolder)
                }
                .addToComposite(compositeDisposable)
    }

    fun requestFolderList(position: Int) {
        compositeDisposable.clear()
        currentUser = users[position]
        preferenceManager.setLastSelectedUserId(currentUser.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { requestFolderList(currentFolder) }
                .addToComposite(compositeDisposable)
    }

    private fun requestFolderList(folder: String) {
        Single.fromCallable {
            val folderRep = FolderDataStorageRemoteImpl(
                    mailDomainRepository.getByNameAndProtocol(
                            getDomainFromEmail(currentUser.address), Protocol.IMAP).blockingGet()
            )
            val allFolders = folderRep.getAll(currentUser).blockingGet()
            val defaultFolders = resources.getStringArray(R.array.folders_default)
            val deff = Folder.values().map { it.name.toLowerCase() }
            val uniqueFolders = allFolders.filter { it.toLowerCase() !in deff }
            (defaultFolders + uniqueFolders).toList()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { folders -> Single.fromCallable {
                    Triple(folders, folder, folders.indexOfFirst { it.toUpperCase() == currentFolder })
                } }
                .subscribe { (folders: List<String>, fldr: String, pos: Int) ->
                    viewState.updateFolderList(folders)
                    viewState.setCurrentFolder(folder, pos)
                    requestUpdateMailList(fldr, 0)
                }
                .addToComposite(compositeDisposable)
    }

    fun requestUpdateMailList(folder: String, offset: Int) {
        compositeDisposable.clear()
        currentFolder = folder
        emailSubscription?.cancel()
        emailSubscription = null
        emailRepositoryFactory.createRepository(currentUser, currentFolder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    emailRepository = it
                    requestUpdateMailList(offset)
                }
    }

    fun requestUpdateMailList(offset: Int) {
        if (emailSubscription == null) {
            emailRepository.getMailHeaders()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        emailSubscription = it
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
        emailSubscription?.request(1)
    }

    fun onEmailClick(messageNumber: Int) {
        viewState.showMessageContent(currentUser, messageNumber)
    }

    fun requestNewEmail() {
        viewState.newEmail(currentUser)
    }

}