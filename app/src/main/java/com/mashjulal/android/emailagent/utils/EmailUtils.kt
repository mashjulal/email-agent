package com.mashjulal.android.emailagent.utils

fun getDomainFromEmail(email: String) = if (email.contains(Regex(".+@[^.]+\\..+")))
    email.substringAfter("@").substringBefore(".") else ""