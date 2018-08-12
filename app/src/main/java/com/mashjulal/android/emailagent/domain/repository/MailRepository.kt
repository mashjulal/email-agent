package com.mashjulal.android.emailagent.domain.repository

import com.mashjulal.android.emailagent.domain.model.Email
import com.mashjulal.android.emailagent.domain.model.User

interface MailRepository {
    fun getMail(user: User, folderName: String, offset: Int, count: Int): List<Email>
    fun getMailByNumber(user: User, folderName: String, number: Int): Email
}