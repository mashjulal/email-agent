package com.mashjulal.android.emailagent.ui.splash

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.AuthActivity
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.main.MainActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), SplashScreenView {

    @Inject
    @InjectPresenter
    lateinit var presenter: SplashScreenPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun navToMailBox() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }

    override fun navToAuth() {
        startActivity(AuthActivity.newIntent(this))
        finish()
    }
}
