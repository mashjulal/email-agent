package com.mashjulal.android.emailagent.utils

object EmailUtils {

    fun getDomainFromEmail(email: String) = if (email.contains(Regex(".+@[^.]+\\..+")))
        email.substringAfter("@").substringBefore(".") else ""
}
