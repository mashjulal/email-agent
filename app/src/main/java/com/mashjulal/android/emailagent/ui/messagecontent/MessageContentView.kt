package com.mashjulal.android.emailagent.ui.messagecontent

import com.mashjulal.android.emailagent.domain.model.EmailContent
import com.mashjulal.android.emailagent.ui.base.BaseView

interface MessageContentView : BaseView {
    fun showMessageTitle(subject: String)
    fun showMessageContent(content: EmailContent)
}