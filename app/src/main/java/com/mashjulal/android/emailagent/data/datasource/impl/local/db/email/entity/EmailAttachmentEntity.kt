package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity

import android.arch.persistence.room.*

@Entity(
        tableName = "email_attachment",
        foreignKeys = [
            ForeignKey(entity = EmailEntity::class, parentColumns = ["id"], childColumns = ["email_id"])
        ],
        indices = [
            Index("email_id")
        ]
)
data class EmailAttachmentEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        @ColumnInfo(name = "email_id")
        val emailId: Long,
        val path: String
)