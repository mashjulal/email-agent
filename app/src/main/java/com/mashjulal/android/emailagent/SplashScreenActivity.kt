package com.mashjulal.android.emailagent

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

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
        val i = MainActivity.newIntent(this, 1)
        return i
    }
}
