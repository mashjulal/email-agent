package com.mashjulal.android.emailagent

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import com.mashjulal.android.emailagent.objects.user.User
import com.mashjulal.android.emailagent.service.MailBoxService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mUser: User
    private var mMailBoxService: MailBoxService? = null
    private val mUiHandler = UIHandler()
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mMailBoxService = (iBinder as MailBoxService.MailBinder).getService()
            update(0)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mMailBoxService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        if (intent != null) {
            parseArguments(intent)
        }
    }

    private fun parseArguments(intent: Intent) {
        val name = intent.getStringExtra(ARG_NAME)
        val email = intent.getStringExtra(ARG_EMAIL)
        val password = intent.getStringExtra(ARG_PASSWORD)
        mUser = User(name, email, password)
    }

    override fun onResume() {
        super.onResume()
        bindService(MailBoxService.newIntent(this), mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
    }

    private fun initRecyclerView() {
        recyclerView.adapter = MailBoxRecyclerViewAdapter(mutableListOf())
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
    }

    private fun update(offset: Int) {
        mMailBoxService!!.requestMessages(this, mUser, offset)
    }

    fun getUIHandler() = mUiHandler

    companion object {

        const val ADD_MESSAGES = 1000

        const val PARAM_MESSAGES = "messages"

        private const val ARG_NAME = "name"
        private const val ARG_EMAIL = "email"
        private const val ARG_PASSWORD = "password"

        fun newIntent(context: Context, name: String, email: String,
                      password: String) : Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(ARG_NAME, name)
            intent.putExtra(ARG_EMAIL, email)
            intent.putExtra(ARG_PASSWORD, password)
            return intent
        }
    }

    inner class UIHandler : Handler() {
        init {
            Handler(Looper.getMainLooper())
        }

        override fun handleMessage(msg: Message?) {
            handleUiMessages(msg!!)
        }
    }

    private fun handleUiMessages(msg: Message) {
        when (msg.what) {
            ADD_MESSAGES -> {
                val messages = msg.data
                        .getParcelableArrayList<com.mashjulal.android.emailagent.objects.mailbox.Message>(PARAM_MESSAGES)
                (recyclerView.adapter as MailBoxRecyclerViewAdapter).onNewData(messages)
            }
        }
    }
}
