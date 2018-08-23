package com.mashjulal.android.emailagent.ui.auth.input

import com.mashjulal.android.emailagent.ui.base.BaseView

interface AuthFormView: BaseView {
    fun completeAuthorization()
    fun showError(error: String)
}