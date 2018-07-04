package com.mashjulal.android.emailagent.authorization

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import kotlinx.android.synthetic.main.fragment_authorization.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AuthorizationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AuthorizationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AuthorizationFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvError: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etEmail = view.et_email
        etEmail.requestFocus()

        etPassword = view.et_password
        view.btn_signIn.setOnClickListener({
            signIn()
        })

        tvError = view.tv_error
    }

    private fun signIn() {
        var fieldsAreValid = true

        val email = etEmail.text.toString()
        if (!isEmailValid(email)) {
            etEmail.error = "Invalid email"
            fieldsAreValid = false
        }

        val password = etPassword.text.toString()
        if (!isPasswordValid(password)) {
            etPassword.error = "Invalid password"
            fieldsAreValid = false
        }

        if (fieldsAreValid) {
            listener?.onSignInButtonClicked(email, password, OnError())
        }
    }

    private fun isEmailValid(email: String)
            = !TextUtils.isEmpty(email) && email.contains('@')

    private fun isPasswordValid(password: String): Boolean = !TextUtils.isEmpty(password)

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onSignInButtonClicked(email: String, password: String, errorHandler: OnError)
    }

    companion object {

        private const val ARG_EMAIL_DOMAIN = "arg-email-domain"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AuthorizationFragment.
         */
        @JvmStatic
        fun newInstance(emailDomain: String): AuthorizationFragment {
            val fragment = AuthorizationFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EMAIL_DOMAIN, emailDomain)
            fragment.arguments = bundle
            return fragment
        }
    }

    inner class OnError {

        fun handle(message: String) {
            tvError.text = message
            tvError.setTextColor(context!!.resources.getColor(android.R.color.holo_red_dark))

            etEmail.error = "Invalid email"
            etPassword.error = "Invalid password"
        }
    }
}
