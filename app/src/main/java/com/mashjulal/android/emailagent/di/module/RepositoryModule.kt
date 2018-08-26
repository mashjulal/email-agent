package com.mashjulal.android.emailagent.di.module

import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.data.datasource.impl.remote.maildomain.MailDomainDataStorageRemoteImpl
import com.mashjulal.android.emailagent.data.datasource.local.prefs.PreferenceManagerImpl
import com.mashjulal.android.emailagent.data.repository.account.AccountRepositoryImpl
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactoryImpl
import com.mashjulal.android.emailagent.data.repository.maildomain.MailDomainRepositoryImpl
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesAccountRepository(accountDataSource: AccountDataSource): AccountRepository
            = AccountRepositoryImpl(accountDataSource)

    @Singleton
    @Provides
    fun providesMailDomainRepository(mailDomainDataStorage: MailDomainDataStorage): MailDomainRepository
            = MailDomainRepositoryImpl(mailDomainDataStorage)

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
    fun providesEmailDataStorageFactory(mailDomainRepository: MailDomainRepository): EmailRepositoryFactory
            = EmailRepositoryFactoryImpl(mailDomainRepository)
}