package com.mashjulal.android.emailagent.domain.model

data class MailDomain(
        val id: Long = 0,
        val name: String,
        val protocol: Protocol,
        val host: String,
        val port: Int,
        val needAuth: Boolean = false
)