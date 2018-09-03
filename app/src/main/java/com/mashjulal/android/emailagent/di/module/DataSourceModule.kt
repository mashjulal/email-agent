package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDataSourceLocalImpl
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainDataSourceLocalImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun providesAccountDataSource(accountDao: AccountDao): AccountDataSource
            = AccountDataSourceLocalImpl(accountDao)

    @Singleton
    @Provides
    @Named("local")
    fun providesEmailDomainDataSource(emailDomainDao: EmailDomainDao): MailDomainDataStorage
            = EmailDomainDataSourceLocalImpl(emailDomainDao)
}