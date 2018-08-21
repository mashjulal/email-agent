package com.mashjulal.android.emailagent.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.AuthActivity

/**
 * Class for activities that shows start image while data is loading.
 */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startActivity(getNextActivityIntent())
        finish()
    }

    private fun getNextActivityIntent(): Intent {
        val i = AuthActivity.newIntent(this)
        return i
    }
}
