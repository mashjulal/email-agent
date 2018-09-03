package com.mashjulal.android.emailagent.ui.auth.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.ui.base.BaseFragment
import com.mashjulal.android.emailagent.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_auth.*
import org.apache.commons.validator.routines.EmailValidator
import javax.inject.Inject

class AuthFormFragment : BaseFragment(), AuthFormView {

    @Inject
    @InjectPresenter
    lateinit var presenter: AuthFormPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnSingIn.setOnClickListener {
            auth()
        }
    }

    private fun auth() {
        tv_error.text = ""

        val name = et_accountName.text.toString()
        val email = et_email.text.toString()
        val pwd = et_password.text.toString()

        val (emailIsValid, msgEmail) = emailValid(email)
        val (passwordIsValid, msgPassword) = passwordValid(email)
        if (emailIsValid && passwordIsValid) {
            presenter.tryToAuth(name, email, pwd)
        } else {
            if (msgEmail.isNotBlank()) {
                et_email.error = msgEmail
            }
            if (msgPassword.isNotBlank()) {
                et_password.error = msgPassword
            }
        }
    }

    private fun emailValid(email: String): Pair<Boolean, String> {
        if (email.isBlank()) {
            return false to "Empty email"
        } else if (!EmailValidator.getInstance().isValid(email)) {
            return false to "Invalid email"
        }
        return true to ""
    }

    private fun passwordValid(pwd: String): Pair<Boolean, String> {
        if (pwd.isBlank()) {
            return false to "Empty password"
        }
        return true to ""
    }

    override fun completeAuthorization() {
        startActivity(MainActivity.newIntent(requireContext()))
    }

    override fun showError(error: String) {
        tv_error.text = error
    }

    companion object {
        fun newInstance() = AuthFormFragment()
    }
}
