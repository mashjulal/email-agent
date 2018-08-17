package com.mashjulal.android.emailagent.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import com.mashjulal.android.emailagent.ui.utils.EndlessRecyclerViewScrollListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {

    @Inject
    lateinit var presenter: MainPresenter

    private lateinit var onEndlessScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.attachView(this)

        intent.let {
            val id = it.getLongExtra(ARG_ID, -1L)
            presenter.requestUser(id)
        }
        initRecyclerView()
        presenter.requestUpdateMailList(0)
    }

    private fun initRecyclerView() {
        recyclerView.adapter = MailBoxRecyclerViewAdapter(mutableListOf()
        ) { messageNumber -> presenter.onEmailClick(messageNumber)}
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))

        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        onEndlessScrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.requestUpdateMailList(page)
            }
        }
        recyclerView.addOnScrollListener(onEndlessScrollListener)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun updateMailList(mail: List<EmailHeader>) {
        (recyclerView.adapter as MailBoxRecyclerViewAdapter).addData(mail)
    }

    override fun stopUpdatingMailList() {
        recyclerView.removeOnScrollListener(onEndlessScrollListener)
    }

    override fun showMessageContent(user: User, messageNumber: Int) {
        startActivity(MessageContentActivity.newIntent(this, user, messageNumber))
    }

    companion object {

        private const val ARG_ID = "id"

        fun newIntent(context: Context, userId: Long) : Intent =
                Intent(context, MainActivity::class.java).apply {
                    putExtra(ARG_ID, userId)
                }
    }
}
