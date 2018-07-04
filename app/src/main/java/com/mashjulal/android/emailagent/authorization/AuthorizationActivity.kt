package com.mashjulal.android.emailagent.authorization

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.mashjulal.android.emailagent.MainActivity
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.service.MailBoxService

class AuthorizationActivity :
        AppCompatActivity(),
        AuthorizationFragment.OnFragmentInteractionListener,
        MailSelectorFragment.OnFragmentInteractionListener{

    companion object {
        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, AuthorizationActivity::class.java)
            return intent
        }
    }

    private var mMailBoxService: MailBoxService? = null
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mMailBoxService = (iBinder as MailBoxService.MailBinder).getService()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mMailBoxService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        showMailSelectorFragment()
    }

    override fun onResume() {
        super.onResume()
        bindService(MailBoxService.newIntent(this), mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
    }

    private fun showMailSelectorFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, MailSelectorFragment.newInstance())
                .commit()
    }

    override fun onSignInButtonClicked(
            email: String, password: String, errorHandler: AuthorizationFragment.OnError) {
        val validUser = mMailBoxService!!.authorize(email, password)
        if (!validUser) {
            errorHandler.handle("Invalid email and/or password")
            return
        }

        startActivity(MainActivity.newIntent(this, "", email, password))
    }

    override fun onMailboxSelected(mailbox: String) {
        showAuthorizationFragment(mailbox)
    }

    private fun showAuthorizationFragment(mailbox: String) {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, AuthorizationFragment.newInstance(mailbox))
                .commit()
    }
}
