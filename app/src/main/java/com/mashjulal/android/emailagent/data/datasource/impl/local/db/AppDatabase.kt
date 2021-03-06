package com.mashjulal.android.emailagent.data.datasource.impl.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.AccountDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAddressDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAttachmentDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAddressEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAttachmentEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainDaoImpl
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainEntity

@Database(
        entities = [
            AccountEntity::class,
            EmailEntity::class,
            EmailAddressEntity::class,
            EmailAttachmentEntity::class,
            FolderEntity::class,
            EmailDomainEntity::class
        ],
        version = 1
)
@TypeConverters(EntityTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAccountDao(): AccountDao
    abstract fun getEmailDao(): EmailDao
    abstract fun getEmailAttachmentDao(): EmailAttachmentDao
    abstract fun getEmailAddressDao(): EmailAddressDao
    abstract fun getFolderDao(): FolderDao
    abstract fun getEmailDomainDao(): EmailDomainDaoImpl
}