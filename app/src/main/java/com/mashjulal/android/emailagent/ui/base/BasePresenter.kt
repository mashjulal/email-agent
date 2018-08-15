package com.mashjulal.android.emailagent.ui.base

open class BasePresenter<V : MvpView>: Presenter<V> {

    var view: V? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    fun isViewAttached() = view != null

    fun checkViewAttached() {
        if (!isViewAttached()) throw MvpViewNotAttachedException()
    }
}

class MvpViewNotAttachedException: RuntimeException(
        "Please call Presenter.attachView(MvpView) before requestiong data to the Presenter"
)