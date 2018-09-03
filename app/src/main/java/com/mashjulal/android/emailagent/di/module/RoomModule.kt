package com.mashjulal.android.emailagent.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.text.Editable
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.AppDatabase
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAddressDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAttachmentDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesAppDatabase(app: Application): AppDatabase {
        val pp = Editable.Factory.getInstance().newEditable(app.getString(R.string.db_k))
        return Room.databaseBuilder(app, AppDatabase::class.java, "appdb")
                .openHelperFactory(SafeHelperFactory.fromUser(pp))
                .build()
    }

    @Singleton
    @Provides
    fun providesAccountDao(db: AppDatabase): AccountDao
            = db.getAccountDao()

    @Singleton
    @Provides
    fun providesEmailDao(db: AppDatabase): EmailDao
            = db.getEmailDao()

    @Singleton
    @Provides
    fun providesEmailAddressDao(db: AppDatabase): EmailAddressDao
            = db.getEmailAddressDao()

    @Singleton
    @Provides
    fun providesEmailAttachmentDao(db: AppDatabase): EmailAttachmentDao
            = db.getEmailAttachmentDao()

    @Singleton
    @Provides
    fun providesFolderDao(db: AppDatabase): FolderDao
            = db.getFolderDao()

    @Singleton
    @Provides
    fun providesEmailDomainDao(db: AppDatabase): EmailDomainDao
            = db.getEmailDomainDao()


}