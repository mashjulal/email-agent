package com.mashjulal.android.emailagent.ui.main

import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.data.repository.mail.FolderRepositoryImpl
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val preferenceManager: PreferenceManager,
        private val accountRepository: AccountRepository
): BasePresenter<MainView>() {

    private lateinit var currentUser: User
    private lateinit var currentFolder: String
    private lateinit var folders: List<String>

    fun requestUserAndFolderList() {
        Single.fromCallable {
            val userId = preferenceManager.getLastSelectedUserId()
            currentUser = accountRepository.getUserById(userId)
            val domain = currentUser.address.substringAfter("@").substringBefore(".")
            val folderRep = FolderRepositoryImpl(
                    mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP)
            )
            folders = folderRep.getAll(currentUser)
            folders
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { fldrs ->
                    view?.updateFolderList(fldrs)
                }
    }

    fun requestUpdateMailList(folder: String, offset: Int) {
        currentFolder = folder
        requestUpdateMailList(offset)
    }

    fun requestUpdateMailList(offset: Int) {
        Single.fromCallable {
            val domain = currentUser.address.substringAfter("@").substringBefore(".")
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
                        { data: List<EmailHeader> -> view?.updateMailList(data)},
                        { _ -> view?.stopUpdatingMailList()}
                )
    }

    fun onEmailClick(messageNumber: Int) {
        view?.showMessageContent(currentUser, messageNumber)
    }

}

interface MainView: MvpView {
    fun updateMailList(mail: List<EmailHeader>)
    fun stopUpdatingMailList()
    fun showMessageContent(user: User, messageNumber: Int)
    fun updateFolderList(folders: List<String>)
}