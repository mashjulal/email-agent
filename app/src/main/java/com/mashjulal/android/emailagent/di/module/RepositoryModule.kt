package com.mashjulal.android.emailagent.di.module

import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.datasource.impl.remote.maildomain.MailDomainDataStorageRemoteImpl
import com.mashjulal.android.emailagent.data.datasource.local.prefs.PreferenceManagerImpl
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.data.repository.impl.account.AccountRepositoryImpl
import com.mashjulal.android.emailagent.data.repository.impl.folder.FolderRepositoryFactory
import com.mashjulal.android.emailagent.data.repository.impl.folder.FolderRepositoryFactoryImpl
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactoryImpl
import com.mashjulal.android.emailagent.data.repository.impl.maildomain.MailDomainRepositoryImpl
import com.mashjulal.android.emailagent.utils.EmailUtils
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesAccountRepository(accountDataSource: AccountDataSource): AccountRepository
            = AccountRepositoryImpl(accountDataSource)

    @Singleton
    @Provides
    fun providesMailDomainRepository(
            dataSourceRemote: MailDomainDataStorage,
            @Named("local") dataSourceLocal: MailDomainDataStorage
    ): MailDomainRepository
            = MailDomainRepositoryImpl(dataSourceRemote, dataSourceLocal)

    @Singleton
    @Provides
    fun providesMailDomainDataSource(dbReference: DatabaseReference): MailDomainDataStorage
            = MailDomainDataStorageRemoteImpl(dbReference)

    @Singleton
    @Provides
    fun providesPreferenceManager(preferences: SharedPreferences): PreferenceManager
            = PreferenceManagerImpl(preferences)

    @Singleton
    @Provides
    fun providesEmailRepositoryFactory(mailDomainRepository: MailDomainRepository,
                                       emailUtils: EmailUtils,
                                       storeUtils: StoreUtils)
            : EmailRepositoryFactory
            = EmailRepositoryFactoryImpl(mailDomainRepository, emailUtils, storeUtils)

    @Singleton
    @Provides
    fun providesFolderRepositoryFactory(mailDomainRepository: MailDomainRepository,
                                        emailUtils: EmailUtils,
                                        storeUtils: StoreUtils)
            : FolderRepositoryFactory
            = FolderRepositoryFactoryImpl(mailDomainRepository, emailUtils, storeUtils)
}