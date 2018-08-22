package com.mashjulal.android.emailagent.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.text.Editable
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.repository.mail.room.AccountDao
import com.mashjulal.android.emailagent.data.repository.mail.room.AppDatabase
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
}