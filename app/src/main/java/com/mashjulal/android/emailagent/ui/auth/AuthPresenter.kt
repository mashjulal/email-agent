package com.mashjulal.android.emailagent.ui.auth

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(

): BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        viewState.navToAuth()
    }
}