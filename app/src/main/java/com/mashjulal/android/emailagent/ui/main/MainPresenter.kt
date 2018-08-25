package com.mashjulal.android.emailagent.ui.main

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.data.repository.mail.FolderRepositoryImpl
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val resources: Resources,
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager,
        private val accountRepository: AccountRepository
): BasePresenter<MainView>() {

    private lateinit var users: List<User>
    private lateinit var currentUser: User
    private var currentFolder: String = Folder.INBOX.name

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
                .subscribe { (users: List<User>, user: User, pos: Int) ->
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
            val folderRep = FolderRepositoryImpl(
                    mailDomainRepository.getByNameAndProtocol(
                            getDomainFromEmail(currentUser.address), Protocol.IMAP)
            )
            val allFolders = folderRep.getAll(currentUser)
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
                    currentFolder = fldr
                    viewState.setCurrentFolder(folder, pos)
                    requestUpdateMailList(0)
                }
                .addToComposite(compositeDisposable)
    }

    fun requestUpdateMailList(folder: String, offset: Int) {
        compositeDisposable.clear()
        currentFolder = folder
        requestUpdateMailList(offset)
    }

    fun requestUpdateMailList(offset: Int) {
        Single.fromCallable {
            val domain = getDomainFromEmail(currentUser.address)
            val mailRep = DefaultMailRepository(
                    currentFolder,
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP),
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.SMTP)
            )
            mailRep.getMailHeaders(currentUser, offset)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<EmailHeader> -> viewState.updateMailList(data)},
                        { _ -> viewState.stopUpdatingMailList()}
                ).addToComposite(compositeDisposable)
    }

    fun onEmailClick(messageNumber: Int) {
        viewState.showMessageContent(currentUser, messageNumber)
    }

}