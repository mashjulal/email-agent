package com.mashjulal.android.emailagent.domain.model

class MailDomain(
        val name: String,
        val protocol: Protocol,
        val host: String,
        val port: Int,
        val needAuth: Boolean = false
)