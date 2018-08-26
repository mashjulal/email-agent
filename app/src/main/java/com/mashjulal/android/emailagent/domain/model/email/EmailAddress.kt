package com.mashjulal.android.emailagent.domain.model.email

import javax.mail.internet.InternetAddress

data class EmailAddress (
        val email: String,
        val name: String
) {
    constructor(address: InternetAddress)
            : this(address.address, address.personal ?: address.address)
}