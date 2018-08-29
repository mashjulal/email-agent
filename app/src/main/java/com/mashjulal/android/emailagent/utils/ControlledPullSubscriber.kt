package com.mashjulal.android.emailagent.utils

import io.reactivex.subscribers.DisposableSubscriber

class ControlledPullSubscriber<T> (
        private val onStart: () -> Unit,
        private val onNext: (T) -> Unit,
        private val onError: (Throwable) -> Unit,
        private val onCompleted: () -> Unit,
        private val initialPackCount: Long = 1
): DisposableSubscriber<T>() {

    override fun onStart() {
        request(initialPackCount)
        onStart.invoke()
    }

    override fun onComplete() {
        onCompleted.invoke()
    }

    override fun onNext(t: T) {
        onNext.invoke(t)
    }

    override fun onError(t: Throwable) {
        onError.invoke(t)
    }

    fun requestMore(n: Long) {
        request(n)
    }
}