package com.mashjulal.android.emailagent.ui.splash

import android.os.Bundle
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.AuthActivity
import com.mashjulal.android.emailagent.ui.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Class for activities that shows start image while data is loading.
 */
class SplashScreenActivity : DaggerAppCompatActivity(), SplashScreenView {

    @Inject
    lateinit var presenter: SplashScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        presenter.attachView(this)
        presenter.onInit()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun showMailBox() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }

    override fun showAuth() {
        startActivity(AuthActivity.newIntent(this))
        finish()
    }
}
