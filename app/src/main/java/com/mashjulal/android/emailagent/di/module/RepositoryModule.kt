package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.data.repository.mail.AccountRepositoryImpl
import com.mashjulal.android.emailagent.data.repository.mail.room.AccountDao
import com.mashjulal.android.emailagent.data.repository.mail.stub.MailDomainRepositoryStub
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
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
    fun providesMailDomainRepository(): MailDomainRepository = MailDomainRepositoryStub()
}