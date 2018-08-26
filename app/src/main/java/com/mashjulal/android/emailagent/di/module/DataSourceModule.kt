package com.mashjulal.android.emailagent.di.module

import com.mashjulal.android.emailagent.data.datasource.api.AccountDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDataSourceLocalImpl
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Singleton
    @Provides
    fun providesAccountDataSource(accountDao: AccountDao): AccountDataSource
            = AccountDataSourceLocalImpl(accountDao)
}