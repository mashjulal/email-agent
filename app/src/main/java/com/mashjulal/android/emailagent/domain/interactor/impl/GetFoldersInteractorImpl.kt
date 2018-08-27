package com.mashjulal.android.emailagent.domain.interactor.impl

import android.content.res.Resources
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.datasource.impl.remote.folder.FolderDataStorageRemoteImpl
import com.mashjulal.android.emailagent.domain.interactor.GetFoldersInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetFoldersInteractorImpl @Inject constructor(
        private val resources: Resources,
        private val accountRepository: AccountRepository,
        private val preferenceManager: PreferenceManager,
        private val mailDomainRepository: MailDomainRepository
        ): GetFoldersInteractor {

    override fun getFoldersWithCurrent(folder: String)
            : Maybe<Triple<List<String>, String, Int>> {
        return preferenceManager.getLastSelectedUserId()
                .flatMapMaybe { accountRepository.getUserById(it) }
                .flatMap {currentUser -> Maybe.fromCallable {
                    val folderRep = FolderDataStorageRemoteImpl(
                            mailDomainRepository.getByNameAndProtocol(
                                    getDomainFromEmail(currentUser.address), Protocol.IMAP).blockingGet()
                    )
                    val allFolders = folderRep.getAll(currentUser).blockingGet()
                    val defaultFolders = resources.getStringArray(R.array.folders_default)
                    val deff = Folder.values().map { it.name.toLowerCase() }
                    val uniqueFolders = allFolders.filter { it.toLowerCase() !in deff }
                    (defaultFolders + uniqueFolders).toList()
                } }
                .flatMap { folders -> Maybe.fromCallable {
                    Triple(folders, folder, folders.indexOfFirst { it.toUpperCase() == folder })
                } }
                .doOnError {
                    Timber.e(it)
                }
    }

    override fun getFoldersWithForNewAccount(account: Account): Maybe<Triple<List<String>, String, Int>> {
        return preferenceManager.setLastSelectedUserId(account.id)
                .subscribeOn(Schedulers.io())
                .andThen(getFoldersWithCurrent(Folder.INBOX.name))
                .doOnError {
                    Timber.e(it)
                }
    }
}