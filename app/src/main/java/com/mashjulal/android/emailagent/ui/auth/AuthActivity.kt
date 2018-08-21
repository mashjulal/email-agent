package com.mashjulal.android.emailagent.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.input.AuthFormFragment
import com.mashjulal.android.emailagent.ui.main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

private const val TAG_AUTH_FORM = "auth-form"

class AuthActivity : DaggerAppCompatActivity(), AuthView, AuthFormFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var presenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        presenter.attachView(this)
        presenter.startAuth()
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun showAuthForm() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AuthFormFragment.newInstance(), TAG_AUTH_FORM)
                .commit()
    }

    override fun completeAuthorization(userId: Long) {
        startActivity(MainActivity.newIntent(this, userId))
    }

    override fun showError(error: String) {
        val authForm = supportFragmentManager.findFragmentByTag(TAG_AUTH_FORM) as AuthFormFragment
        authForm.showError(error)
    }

    override fun onFragmentInteraction(email: String, password: String) {
        presenter.tryToAuth(email, password)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
