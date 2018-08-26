package com.mashjulal.android.emailagent.data.datasource.impl.local.db.account

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface AccountDao {

    @Insert
    fun insert(account: AccountEntity): Long

    @Query("SELECT * FROM account")
    fun getAll(): Single<List<AccountEntity>>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getById(id: Long): Maybe<AccountEntity>
}