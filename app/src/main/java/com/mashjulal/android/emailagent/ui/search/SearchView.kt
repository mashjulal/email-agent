package com.mashjulal.android.emailagent.ui.search

import com.arellomobile.mvp.MvpView
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader

interface SearchView: MvpView {
    fun showResults(results: List<EmailHeader>)
    fun showMessageContent(user: Account, messageNumber: Int)
    fun updateUserList(users: List<Account>)
    fun setCurrentUser(user: Account, position: Int)
    fun showError(error: String)
}