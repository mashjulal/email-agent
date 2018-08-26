package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAddressEntity
import io.reactivex.Maybe

@Dao
interface EmailAddressDao {

    @Query("""
        SELECT *
        FROM email_address
        WHERE id = :id
    """)
    fun getById(id: Long)
            : Maybe<EmailAddressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(emailAddressEntity: EmailAddressEntity): Long

    @Query("DELETE FROM email_address WHERE id = :id")
    fun delete(id: Long)
}