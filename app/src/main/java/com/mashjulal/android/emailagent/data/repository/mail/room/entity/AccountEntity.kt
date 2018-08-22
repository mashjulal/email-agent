package com.mashjulal.android.emailagent.data.repository.mail.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "account",
        indices = [Index(value = ["email"], unique = true)])
data class AccountEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val email: String,
        val pwd: String
)