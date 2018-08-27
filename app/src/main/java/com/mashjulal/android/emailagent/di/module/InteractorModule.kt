package com.mashjulal.android.emailagent.di.module

import android.content.res.Resources
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.*
import com.mashjulal.android.emailagent.domain.interactor.impl.*
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import dagger.Module
import dagger.Provides

@Module
class InteractorModule {

    @Provides
    fun providesAuthInteractor(accountRepository: AccountRepository,
                               mailDomainRepository: MailDomainRepository,
                               preferenceManager: PreferenceManager): AuthInteractor
            = AuthInteractorImpl(accountRepository, mailDomainRepository, preferenceManager)

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
                                     mailDomainRepository: MailDomainRepository)
            : GetFoldersInteractor
            = GetFoldersInteractorImpl(resources, accountRepository,
            preferenceManager, mailDomainRepository)

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