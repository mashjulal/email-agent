package com.mashjulal.android.emailagent.data.repository.mail.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mashjulal.android.emailagent.data.repository.mail.room.entity.AccountEntity

@Database(entities = [AccountEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAccountDao(): AccountDao
}