package com.mashjulal.android.emailagent.domain.interactor.impl

import android.content.res.Resources
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.data.repository.impl.folder.FolderRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.GetFoldersInteractor
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class GetFoldersInteractorImpl @Inject constructor(
        private val resources: Resources,
        private val accountRepository: AccountRepository,
        private val preferenceManager: PreferenceManager,
        private val folderRepositoryFactory: FolderRepositoryFactory
        ): GetFoldersInteractor {

    override fun getFoldersWithCurrent(folder: String)
            : Single<Triple<List<String>, String, Int>> {
        return preferenceManager.getLastSelectedUserId()
                .flatMap { accountRepository.getUserById(it) }
                .flatMap {currentUser -> Single.fromCallable {
                    val folderRep = folderRepositoryFactory.createFolderRepository(currentUser).blockingGet()
                    val allFolders = folderRep.getAll(currentUser).blockingGet()
                    val defaultFolders = resources.getStringArray(R.array.folders_default)
                    val deff = Folder.values().map { it.name.toLowerCase() }
                    val uniqueFolders = allFolders.filter { it.toLowerCase() !in deff }
                    (defaultFolders + uniqueFolders).toList()
                } }
                .flatMap { folders -> Single.fromCallable {
                    Triple(folders, folder, folders.indexOfFirst { it.toUpperCase() == folder })
                } }
                .doOnError {
                    Timber.e(it)
                }
    }

    override fun getFoldersWithForNewAccount(account: Account): Single<Triple<List<String>, String, Int>> {
        return preferenceManager.setLastSelectedUserId(account.id)
                .subscribeOn(Schedulers.io())
                .andThen(getFoldersWithCurrent(Folder.INBOX.name))
                .doOnError {
                    Timber.e(it)
                }
    }
}