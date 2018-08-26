package com.mashjulal.android.emailagent.domain.model.email

import java.io.InputStream

data class EmailAttachment (
        val filename: String,
        val contentType: String,
        val inputStream: InputStream
)