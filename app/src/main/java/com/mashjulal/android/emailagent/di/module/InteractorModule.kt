package com.mashjulal.android.emailagent.di.module

import android.content.res.Resources
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.data.repository.impl.folder.FolderRepositoryFactory
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.*
import com.mashjulal.android.emailagent.domain.interactor.impl.*
import com.mashjulal.android.emailagent.utils.EmailUtils
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun providesAuthInteractor(accountRepository: AccountRepository,
                               mailDomainRepository: MailDomainRepository,
                               preferenceManager: PreferenceManager,
                               emailUtils: EmailUtils,
                               storeUtils: StoreUtils

    ): AuthInteractor
            = AuthInteractorImpl(accountRepository, mailDomainRepository,
            preferenceManager, emailUtils, storeUtils)

    @Provides
    fun providesGetAccountsWithCurrentInteractor(accountRepository: AccountRepository,
                                                 preferenceManager: PreferenceManager)
            : GetAccountsWithCurrentInteractor
            = GetAccountsWithCurrentInteractorImpl(preferenceManager, accountRepository)

    @Provides
    fun providesGetEmailHeadersInteractor(emailRepositoryFactory: EmailRepositoryFactory)
            : GetEmailHeadersInteractor
            = GetEmailHeadersInteractorImpl(emailRepositoryFactory)

    @Provides
    fun providesGetFoldersInteractor(resources: Resources,
                                     accountRepository: AccountRepository,
                                     preferenceManager: PreferenceManager,
                                     folderRepositoryFactory: FolderRepositoryFactory)
            : GetFoldersInteractor
            = GetFoldersInteractorImpl(resources, accountRepository,
            preferenceManager, folderRepositoryFactory)

    @Provides
    fun providesSendEmailInteractor(accountRepository: AccountRepository,
                                    emailRepositoryFactory: EmailRepositoryFactory)
            : SendEmailInteractor
            = SendEmailInteractorImpl(accountRepository, emailRepositoryFactory)

    @Provides
    fun providesGetEmailContentInteractor(accountRepository: AccountRepository,
                                          emailRepositoryFactory: EmailRepositoryFactory)
            : GetEmailContentInteractor
            = GetEmailContentInteractorImpl(accountRepository, emailRepositoryFactory)
}