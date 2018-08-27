package com.mashjulal.android.emailagent.ui.splash

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.repository.PreferenceManager
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class SplashScreenPresenter @Inject constructor(
        private val preferenceManager: PreferenceManager
) : BasePresenter<SplashScreenView>() {

    fun onInit() {
        preferenceManager.isAnyUserLogged()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isLogged ->
                    if (isLogged) viewState.navToMailBox() else viewState.navToAuth()
                }
    }
}

