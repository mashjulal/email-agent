package com.mashjulal.android.emailagent.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.auth.input.AuthFormFragment
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import javax.inject.Inject

private const val TAG_AUTH_FORM = "auth-form"

class AuthActivity : BaseActivity(), AuthView {

    @Inject
    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun navToAuth() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, AuthFormFragment.newInstance(), TAG_AUTH_FORM)
                .commit()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }
}
