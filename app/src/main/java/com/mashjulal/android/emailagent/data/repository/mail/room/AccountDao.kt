package com.mashjulal.android.emailagent.data.repository.mail.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mashjulal.android.emailagent.data.repository.mail.room.entity.AccountEntity

@Dao
interface AccountDao {

    @Insert
    fun insert(account: AccountEntity): Long

    @Query("SELECT * FROM account")
    fun getAll(): List<AccountEntity>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getById(id: Long): AccountEntity
}