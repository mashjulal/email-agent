package com.mashjulal.android.emailagent.domain.model

class MailDomain(
        val id: Long,
        val name: String,
        val protocol: String,
        val host: String,
        val port: Int,
        val needAuth: Boolean = false
)