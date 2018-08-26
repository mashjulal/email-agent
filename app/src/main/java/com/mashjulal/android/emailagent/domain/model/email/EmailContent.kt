package com.mashjulal.android.emailagent.domain.model.email

data class EmailContent (
        val textContent: String,
        val htmlContent: String,
        val attachments: List<EmailAttachment>
)