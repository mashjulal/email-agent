package com.mashjulal.android.emailagent.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.main.MailBoxRecyclerViewAdapter
import com.mashjulal.android.emailagent.ui.main.menu.AccountListAdapter
import com.mashjulal.android.emailagent.ui.messagecontent.MessageContentActivity
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity(), SearchView {

    @Inject
    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var accountAdapter: AccountListAdapter

    private var accountId: Long = 0
    private lateinit var folder: String
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        presenter.onInit(accountId, folder, query)
    }

    private fun parseArgs() {
        intent.let {
            accountId = it.getLongExtra(ARG_ACCOUNT_ID, 0)
            folder = it.getStringExtra(ARG_FOLDER)
            query = it.getStringExtra(ARG_QUERY)
        }
    }

    fun search() {
        progressBar.visibility = View.VISIBLE
        presenter.search(accountId, folder, query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.mi_action_search).actionView
                as android.support.v7.widget.SearchView
        searchView.onActionViewExpanded()
        searchView.setQuery(query, false)
        searchView.setOnQueryTextListener(object: android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                this@SearchActivity.query = query
                search()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })
        return true
    }

    override fun showResults(results: List<EmailHeader>) {
        val adapter = MailBoxRecyclerViewAdapter(this, results.toMutableList())
        { messageNumber -> presenter.onEmailClick(messageNumber)}
        adapter.setFooter(MailBoxRecyclerViewAdapter.FooterType.NO_RESULTS)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        progressBar.visibility = View.GONE
    }

    override fun showMessageContent(user: Account, messageNumber: Int) {
        startActivity(MessageContentActivity.newIntent(this, user, messageNumber))
    }

    override fun updateUserList(users: List<Account>) {
        setContentView(R.layout.activity_search)

        if (!this::accountAdapter.isInitialized) {
            accountAdapter = AccountListAdapter(users) {
                accountId = it.id
            }
            rvAccounts.adapter = accountAdapter
        }
    }

    override fun setCurrentUser(user: Account, position: Int) {
        accountAdapter.setSelected(position)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_ACCOUNT_ID = "account-id"
        private const val ARG_FOLDER = "folder"
        private const val ARG_QUERY = "query"

        fun newIntent(context: Context, accountId: Long, folder: String, query: String) =
                Intent(context, SearchActivity::class.java).apply {
                    putExtra(ARG_ACCOUNT_ID, accountId)
                    putExtra(ARG_FOLDER, folder)
                    putExtra(ARG_QUERY, query)
                }
    }
}
