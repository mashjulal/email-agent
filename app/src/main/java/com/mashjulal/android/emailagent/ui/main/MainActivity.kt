package com.mashjulal.android.emailagent.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import com.mashjulal.android.emailagent.ui.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_message_navigation_drawer.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var onEndlessScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var mailListAdapter: MailBoxRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        initRecyclerView()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        mailListAdapter = MailBoxRecyclerViewAdapter(mutableListOf())
        { messageNumber -> presenter.onEmailClick(messageNumber)}
                recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        recyclerView.adapter = mailListAdapter

        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        onEndlessScrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.requestUpdateMailList(page)
            }
        }
        recyclerView.addOnScrollListener(onEndlessScrollListener)
    }

    override fun updateMailList(mail: List<EmailHeader>) {
        mailListAdapter.addData(mail)
    }

    override fun stopUpdatingMailList() {
        recyclerView.removeOnScrollListener(onEndlessScrollListener)
    }

    override fun showMessageContent(user: User, messageNumber: Int) {
        startActivity(MessageContentActivity.newIntent(this, user, messageNumber))
    }

    override fun updateFolderList(folders: List<String>) {
        val defaultFolders = resources.getStringArray(R.array.folders_default)
        val deff = Folder.values().map { it.name.toLowerCase() }
        val domainUniqueFolders = folders.filter { it.toLowerCase() !in deff }

        lv_folders.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                defaultFolders + domainUniqueFolders)
        lv_folders.setOnItemClickListener { _, _, position, _ ->
            mailListAdapter.clear()
            onEndlessScrollListener.resetState()
            recyclerView.removeOnScrollListener(onEndlessScrollListener)
            recyclerView.addOnScrollListener(onEndlessScrollListener)

            val selectedFolder = lv_users.getItemAtPosition(position) as String
            presenter.requestUpdateMailList(selectedFolder, 0)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun updateUserList(users: List<User>) {
        lv_users.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                users.map { it.address })
        lv_users.setOnItemClickListener { _, _, position, _ ->
            mailListAdapter.clear()
            onEndlessScrollListener.resetState()
            recyclerView.removeOnScrollListener(onEndlessScrollListener)
            recyclerView.addOnScrollListener(onEndlessScrollListener)

            val selectedFolder = Folder.INBOX.name
            presenter.requestUpdateMailList(selectedFolder, 0)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    companion object {

        fun newIntent(context: Context) : Intent =
                Intent(context, MainActivity::class.java)
    }
}
