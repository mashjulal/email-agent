package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailEntity
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface EmailDao {

    @Query("""
        SELECT *
        FROM email
        WHERE account_id = :accountId
            AND folder_id = :folderId
            AND message_number = :messageNumber
    """)
    fun getByAccountFolderAndMessageNumber(accountId: Long, folderId: Long, messageNumber: Int)
            : Maybe<EmailEntity>

    @Query("""
        SELECT *
        FROM email
        WHERE account_id = :accountId
            AND folder_id = :folderId
        ORDER BY received_at DESC
    """)
    fun getAllByAccountAndFolderOrderByReceiveDateDesc(accountId: Long, folderId: Long)
            : Flowable<List<EmailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(emailEntity: EmailEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg emailEntity: EmailEntity): List<Long>

    @Query("DELETE FROM email WHERE id = :id")
    fun delete(id: Long)
}
