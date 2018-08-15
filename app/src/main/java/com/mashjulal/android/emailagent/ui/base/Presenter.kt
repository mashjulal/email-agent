package com.mashjulal.android.emailagent.ui.base

interface Presenter<V: MvpView> {
    fun attachView(view: V)
    fun detachView()
}