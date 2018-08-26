package com.mashjulal.android.emailagent.ui.newemail

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BaseView

interface NewEmailView : BaseView {
    fun setAccounts(accounts: List<Account>, selectedPosition: Int)
    fun close()
}