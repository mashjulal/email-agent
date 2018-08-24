package com.mashjulal.android.emailagent.utils

fun getDomainFromEmail(email: String)
        = email.substringAfter("@").substringBefore(".")