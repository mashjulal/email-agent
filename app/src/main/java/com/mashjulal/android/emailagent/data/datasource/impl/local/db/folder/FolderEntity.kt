package com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity

@Entity(
        tableName = "folder",
        foreignKeys = [
                ForeignKey(entity = AccountEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["account_id"],
                        onDelete = CASCADE)
        ],
        indices = [
            Index("account_id")
        ]
)
data class FolderEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        @ColumnInfo(name = "account_id")
        val accountId: Long,
        val name: String
)