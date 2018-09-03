package com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.mashjulal.android.emailagent.domain.model.Protocol

@Entity(
        tableName = "email_domain",
        indices = [
            Index(value = ["name", "protocol"], unique = true)
        ]
)
data class EmailDomainEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val name: String,
        val host: String,
        val port: Int,
        val protocol: Protocol,
        @ColumnInfo(name = "need_auth")
        val needAuth: Boolean
)