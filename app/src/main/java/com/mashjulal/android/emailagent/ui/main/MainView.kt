package com.mashjulal.android.emailagent.ui.main

import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.ui.base.BaseView

interface MainView : BaseView {
    fun updateMailList(mail: List<EmailHeader>)
    fun stopUpdatingMailList()
    fun showMessageContent(user: User, messageNumber: Int)
    fun updateFolderList(folders: List<String>)
}