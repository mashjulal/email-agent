package com.mashjulal.android.emailagent.ui.splash

import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import javax.inject.Inject

class SplashScreenPresenter @Inject constructor(
        private val preferenceManager: PreferenceManager
) : BasePresenter<SplashScreenView>() {

    fun onInit() {
        if (preferenceManager.isAnyUserLogged()) {
            view?.showMailBox()
        } else {
            view?.showAuth()
        }
    }

}

interface SplashScreenView : MvpView {
    fun showMailBox()
    fun showAuth()
}