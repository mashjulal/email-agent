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

        val email = et_email.text.toString()
        val pwd = et_password.text.toString()
        if (email.isBlank() || pwd.isBlank()) {
            if (email.isBlank()) {
                et_email.error = "Empty email"
            } else {
                et_password.error = "Empty password"
            }
            return
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            et_email.error = "Invalid email"
            return
        }
        presenter.tryToAuth(email, pwd)
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
