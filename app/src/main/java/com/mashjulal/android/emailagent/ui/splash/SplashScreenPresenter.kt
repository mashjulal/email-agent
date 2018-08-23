package com.mashjulal.android.emailagent.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SplashScreenPresenter @Inject constructor(
        private val preferenceManager: PreferenceManager
) : BasePresenter<SplashScreenView>() {

    override fun onFirstViewAttach() {
        if (preferenceManager.isAnyUserLogged()) {
            viewState.navToMailBox()
        } else {
            viewState.navToAuth()
        }
    }
}

