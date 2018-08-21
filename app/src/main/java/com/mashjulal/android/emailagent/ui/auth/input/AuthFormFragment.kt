package com.mashjulal.android.emailagent.ui.auth.input

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashjulal.android.emailagent.R
import kotlinx.android.synthetic.main.fragment_auth.*
import org.apache.commons.validator.routines.EmailValidator

class AuthFormFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

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

        listener?.onFragmentInteraction(email, pwd)
    }

    fun showError(error: String) {
        tv_error.text = error
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(email: String, password: String)
    }

    companion object {
        fun newInstance() = AuthFormFragment()
    }
}
