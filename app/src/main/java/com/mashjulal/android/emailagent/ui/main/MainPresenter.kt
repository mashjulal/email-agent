package com.mashjulal.android.emailagent.ui.main

import com.arellomobile.mvp.InjectViewState
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
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager,
        private val accountRepository: AccountRepository
): BasePresenter<MainView>() {

    private lateinit var currentUser: User
    private lateinit var currentFolder: String
    private lateinit var folders: List<String>

    override fun onFirstViewAttach() {
        requestUserAndFolderList()
        requestUserList()
        requestUpdateMailList(Folder.INBOX.name, 0)
    }

    private fun requestUserAndFolderList() {
        preferenceManager.getLastSelectedUserId()
                .flatMapMaybe { userId -> accountRepository.getUserById(userId) }
                .flatMapSingle {
                    Single.fromCallable {
                        currentUser = it
                        val folderRep = FolderRepositoryImpl(
                                mailDomainRepository.getByNameAndProtocol(
                                        getDomainFromEmail(currentUser.address), Protocol.IMAP)
                        )
                        folders = folderRep.getAll(currentUser)
                        folders
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { fldrs ->
                    viewState.updateFolderList(fldrs)
                }.addToComposite(compositeDisposable)
    }

    private fun requestUserList() {
        accountRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users ->
                    viewState.updateUserList(users)
                }.addToComposite(compositeDisposable)
    }

    fun requestUpdateMailList(folder: String, offset: Int) {
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