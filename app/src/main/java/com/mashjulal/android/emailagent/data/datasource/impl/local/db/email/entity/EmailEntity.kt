package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.ForeignKey.NO_ACTION
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderEntity

@Entity(
        tableName = "email",
        foreignKeys = [
            ForeignKey(entity = AccountEntity::class,
                    parentColumns = ["id"], childColumns = ["account_id"],
                    onUpdate = NO_ACTION, onDelete = CASCADE),
            ForeignKey(entity = FolderEntity::class,
                    parentColumns = ["id"], childColumns = ["folder_id"],
                    onUpdate = NO_ACTION, onDelete = CASCADE),
            ForeignKey(entity = EmailAddressEntity::class,
                    parentColumns = ["id"], childColumns = ["from_address_id"],
                    onUpdate = NO_ACTION, onDelete = NO_ACTION)
        ],
        indices = [
            Index("account_id"),
            Index("folder_id"),
            Index("from_address_id"),
            Index("account_id", "folder_id", "message_number", unique = true)
        ]
)
data class EmailEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "account_id")
        val accountId: Long,
        @ColumnInfo(name = "folder_id")
        val folderId: Long,
        @ColumnInfo(name = "message_number")
        val messageNumber: Int,
        val subject: String,
        @ColumnInfo(name = "from_address_id")
        val fromAddressId: Long,
        @ColumnInfo(name = "received_at")
        val receivedDate: Long,
        val isRead: Boolean,
        @ColumnInfo(name = "text_content")
        val textContent: String,
        @ColumnInfo(name = "html_content")
        val htmlContent: String

)