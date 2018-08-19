package com.mashjulal.android.emailagent.ui.main

import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.data.repository.mail.FolderRepositoryImpl
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val accountRepository: AccountRepository
): BasePresenter<MainView>() {

    private lateinit var currentUser: User
    private lateinit var currentFolder: String
    private lateinit var folders: List<String>

    fun requestUserAndFolderList(userId: Long) {
        Single.fromCallable {
            currentUser = accountRepository.getUserById(userId)

            val domains = mailDomainRepository.getByName("yandex")
            val folderRep = FolderRepositoryImpl(
                    domains.first { it.protocol == "imap" }
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
            val domains = mailDomainRepository.getByName("yandex")
            val mailRep = DefaultMailRepository(
                    currentFolder,
                    domains.first { it.protocol == "imap" },
                    domains.first { it.protocol == "smtp" }
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