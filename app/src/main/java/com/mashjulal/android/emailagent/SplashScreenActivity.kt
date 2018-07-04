package com.mashjulal.android.emailagent

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mashjulal.android.emailagent.authorization.AuthorizationActivity

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

    private fun isAuthorized(): Boolean {
        // TODO: remove stub
        return false
    }

    private fun getNextActivityIntent(): Intent {
        val isAuthorized = isAuthorized()
        // TODO: select next activity
        val i = AuthorizationActivity.newIntent(this)
        return i
    }
}
