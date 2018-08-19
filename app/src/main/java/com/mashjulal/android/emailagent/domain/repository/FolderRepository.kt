package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.User

interface FolderRepository {
    fun getAll(user: User): List<String>
}

