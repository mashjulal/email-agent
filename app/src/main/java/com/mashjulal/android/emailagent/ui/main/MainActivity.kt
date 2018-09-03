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
import android.support.v7.widget.SearchView
import android.view.Menu
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.ui.auth.AuthActivity
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.main.menu.AccountListAdapter
import com.mashjulal.android.emailagent.ui.main.menu.FolderListAdapter
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import com.mashjulal.android.emailagent.ui.newemail.NewEmailActivity
import com.mashjulal.android.emailagent.ui.search.SearchActivity
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
    private lateinit var userListAdapter: AccountListAdapter
    private lateinit var folderListAdapter: FolderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        btn_newUser.setOnClickListener {
            startActivityForResult(AuthActivity.newIntent(this), 0)
        }
        fab_newEmail.setOnClickListener {
            presenter.requestNewEmail()
        }
        swipeRefresh.setOnRefreshListener {
            mailListAdapter.clear()
            onEndlessScrollListener.resetState()
            recyclerView.removeOnScrollListener(onEndlessScrollListener)
            recyclerView.addOnScrollListener(onEndlessScrollListener)

            val selectedFolder = folderListAdapter.getSelected()
            presenter.requestUpdateMailList(selectedFolder, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.mi_action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.onStartSearch(query)
                searchView.onActionViewCollapsed()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun initMailList() {
        mailListAdapter = MailBoxRecyclerViewAdapter(this, mutableListOf())
        { messageNumber -> presenter.onEmailClick(messageNumber)}
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        recyclerView.adapter = mailListAdapter

        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        onEndlessScrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.requestUpdateMailList(offset = page)
            }
        }
        recyclerView.addOnScrollListener(onEndlessScrollListener)
    }

    override fun updateMailList(mail: List<EmailHeader>) {
        mailListAdapter.addData(mail)
        swipeRefresh.isRefreshing = false
    }

    override fun stopUpdatingMailList() {
        recyclerView.removeOnScrollListener(onEndlessScrollListener)
        mailListAdapter.setFooter(MailBoxRecyclerViewAdapter.FooterType.NO_RESULTS)
    }

    override fun showMessageContent(user: Account, messageNumber: Int) {
        startActivity(MessageContentActivity.newIntent(this, user, messageNumber))
    }

    override fun updateFolderList(folders: List<String>) {
        folderListAdapter = FolderListAdapter(folders) {
            mailListAdapter.clear()
            onEndlessScrollListener.resetState()
            recyclerView.removeOnScrollListener(onEndlessScrollListener)
            recyclerView.addOnScrollListener(onEndlessScrollListener)

            val selectedFolder = folderListAdapter.getSelected()
            setCurrentFolder(selectedFolder, it)
            presenter.requestUpdateMailList(selectedFolder, 0)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        rv_folders.adapter = folderListAdapter
        rv_folders.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun updateUserList(users: List<Account>) {
        userListAdapter = AccountListAdapter(users) {
            mailListAdapter.clear()
            onEndlessScrollListener.resetState()
            recyclerView.removeOnScrollListener(onEndlessScrollListener)
            recyclerView.addOnScrollListener(onEndlessScrollListener)

            presenter.requestFolderList(it)
            drawer_layout.closeDrawer(GravityCompat.START)

            setCurrentUser(it, userListAdapter.getSelected())
        }
        rv_accounts.adapter = userListAdapter
    }

    override fun setCurrentUser(user: Account, position: Int) {
        tv_name.text = user.name
        tv_email.text = user.address

        userListAdapter.setSelected(position)
    }

    override fun setCurrentFolder(folder: String, position: Int) {
        folderListAdapter.setSelected(position)
        supportActionBar?.title = folder
    }

    override fun newEmail() {
        startActivity(NewEmailActivity.newIntent(this))
    }

    override fun navToSearchScreen(accountId: Long, folder: String, query: String) {
        startActivity(SearchActivity.newIntent(this, accountId, folder, query))
    }

    companion object {

        fun newIntent(context: Context) : Intent =
                Intent(context, MainActivity::class.java)
    }
}
