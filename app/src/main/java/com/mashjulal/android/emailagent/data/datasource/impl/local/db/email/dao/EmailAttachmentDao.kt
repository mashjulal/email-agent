package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailAttachmentEntity
import io.reactivex.Single

@Dao
interface EmailAttachmentDao {

    @Query("""
        SELECT *
        FROM email_attachment
        WHERE id = :id
    """)
    fun getById(id: Long)
            : Single<EmailAttachmentEntity>

    @Query("""
        SELECT *
        FROM email_attachment
        WHERE email_id = :emailId
    """)
    fun getAllByEmailId(emailId: Long)
            : Single<List<EmailAttachmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(emailAttachmentEntity: EmailAttachmentEntity): Long

    @Query("DELETE FROM email_attachment WHERE id = :id")
    fun delete(id: Long)
}