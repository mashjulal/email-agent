package com.mashjulal.android.emailagent.ui.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.AuthActivity
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.main.MainActivity
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), SplashScreenView {

    companion object {
        private const val RC_PERMISSION_WRITE_EXT = 1
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SplashScreenPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    RC_PERMISSION_WRITE_EXT)
        } else {
            presenter.onInit()
        }
    }

    override fun navToMailBox() {
        startActivity(MainActivity.newIntent(this))
        finish()
    }

    override fun navToAuth() {
        startActivity(AuthActivity.newIntent(this))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_WRITE_EXT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.onInit()
                } else {
                    finish()
                }
            }
        }
    }
}
