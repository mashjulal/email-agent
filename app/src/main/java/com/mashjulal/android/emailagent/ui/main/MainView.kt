package com.mashjulal.android.emailagent.ui.main

import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.ui.base.BaseView

interface MainView : BaseView {
    fun updateMailList(mail: List<EmailHeader>)
    fun stopUpdatingMailList()
    fun showMessageContent(user: Account, messageNumber: Int)
    fun updateFolderList(folders: List<String>)
    fun updateUserList(users: List<Account>)
    fun setCurrentUser(user: Account, position: Int)
    fun setCurrentFolder(folder: String, position: Int)
}