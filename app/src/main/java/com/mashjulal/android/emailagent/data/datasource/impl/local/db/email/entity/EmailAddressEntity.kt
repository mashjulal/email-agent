package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "email_address")
data class EmailAddressEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val email: String,
        val name: String
)