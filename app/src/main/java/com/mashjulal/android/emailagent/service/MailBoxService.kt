package com.mashjulal.android.emailagent.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.mashjulal.android.emailagent.MainActivity
import com.mashjulal.android.emailagent.WorkerThread
import com.mashjulal.android.emailagent.objects.mailbox.Message
import com.mashjulal.android.emailagent.objects.mailbox.StandardMailboxFactory
import com.mashjulal.android.emailagent.objects.user.User
import java.util.*
import javax.mail.Folder
import kotlin.math.min

class MailBoxService : Service() {

    companion object {
        private const val WORKER_THREAD_NAME = "mailWorkerThread"
        private const val MODE = START_NOT_STICKY
        private const val PAGE_SIZE = 10

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, MailBoxService::class.java)
            return intent
        }
    }
    private lateinit var mailBinder: MailBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return MODE
    }

    override fun onBind(intent: Intent): IBinder {
        if (!::mailBinder.isInitialized) {
            mailBinder = MailBinder()
        }
        return mailBinder
    }

    fun authorize(email: String, password: String): Boolean {
        if (false) {
            return false
        }
        return true
    }

    fun requestMessages(context: Context, user: User, offset: Int) {
        val mailBox = StandardMailboxFactory.createFromUser(user)
        val handlerThread = WorkerThread(WORKER_THREAD_NAME)
        handlerThread.start()
        handlerThread.prepareHandler()
        handlerThread.postTask({
            val folder = mailBox.getInboxFolder()
            folder.open(Folder.READ_ONLY)
            val messageCount = folder.messageCount
            val messages = folder.messages
                    .reversedArray()
                    .copyOfRange(offset, min(offset + PAGE_SIZE, messageCount))
                    .map { Message(it) }
            folder.close()
            folder.store.close()

            val msg = android.os.Message()
            msg.what = MainActivity.ADD_MESSAGES
            msg.data.putParcelableArrayList(MainActivity.PARAM_MESSAGES, messages as ArrayList<Message>)
            (context as MainActivity).getUIHandler().sendMessage(msg)
        })
    }

    inner class MailBinder : Binder() {

        fun getService() : MailBoxService = this@MailBoxService
    }
}
