package com.mashjulal.android.emailagent

import android.os.Handler
import android.os.HandlerThread

class WorkerThread(name: String): HandlerThread(name) {

    private lateinit var mWorkerHandler: Handler

    fun postTask(task: () -> Unit) {
        mWorkerHandler.post(task)
    }

    fun prepareHandler() {
        mWorkerHandler = Handler(looper)
    }
}