package com.mashjulal.android.emailagent.authorization

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mashjulal.android.emailagent.R
import kotlinx.android.synthetic.main.fragment_mail_selector.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MailSelectorFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MailSelectorFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MailSelectorFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mail_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.btn_gmail.setOnClickListener { listener!!.onMailboxSelected("Gmail") }
        view.btn_yandex.setOnClickListener { listener!!.onMailboxSelected("Yandex") }
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
        fun onMailboxSelected(mailbox: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MailSelectorFragment.
         */
        @JvmStatic
        fun newInstance() = MailSelectorFragment()
    }
}
