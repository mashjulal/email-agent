package com.mashjulal.android.emailagent.di.module

import android.content.SharedPreferences
import com.google.firebase.database.DatabaseReference
import com.mashjulal.android.emailagent.data.repository.mail.AccountRepositoryImpl
import com.mashjulal.android.emailagent.data.repository.mail.MailDomainRepositoryImpl
import com.mashjulal.android.emailagent.data.repository.mail.room.AccountDao
import com.mashjulal.android.emailagent.data.repository.prefs.PreferenceManagerImpl
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
    fun providesAccountRepository(accountDao: AccountDao): AccountRepository
            = AccountRepositoryImpl(accountDao)

    @Singleton
    @Provides
    fun providesMailDomainRepository(db: DatabaseReference): MailDomainRepository
            = MailDomainRepositoryImpl(db)

    @Singleton
    @Provides
    fun providesPreferenceManager(preferences: SharedPreferences): PreferenceManager
            = PreferenceManagerImpl(preferences)
}