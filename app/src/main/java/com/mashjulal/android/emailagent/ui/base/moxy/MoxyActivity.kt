package com.mashjulal.android.emailagent.ui.base.moxy

import android.annotation.SuppressLint
import android.os.Bundle
import com.arellomobile.mvp.MvpDelegate
import dagger.android.support.DaggerAppCompatActivity



@SuppressLint("Registered")
open class MoxyActivity : DaggerAppCompatActivity() {
    private var mMvpDelegate: MvpDelegate<out MoxyActivity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        getMvpDelegate().onDestroyView()

        if (isFinishing) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    fun getMvpDelegate(): MvpDelegate<*> {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate(this)
        }
        return mMvpDelegate!!
    }
}