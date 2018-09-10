package com.mashjulal.android.emailagent.ui.newemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_email.*
import javax.inject.Inject

class NewEmailActivity : BaseActivity(), NewEmailView {

    @Inject
    @InjectPresenter
    lateinit var presenter: NewEmailPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var accountListAdapter: AccountListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_email)

        presenter.onInit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_email, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_action_done -> {
                collectEmail()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun collectEmail() {
        val account = spnrAccounts.selectedItem as Account
        val to = etTo.text.toString()
        val subject = etSubject.text.toString()
        val content = etText.text.toString()
        val subscription = etSubscription.text.toString()
        presenter.sendEmail(account, to, subject, content, subscription)
    }

    override fun setAccounts(accounts: List<Account>, selectedPosition: Int) {
        accountListAdapter = AccountListAdapter(this, accounts)
        spnrAccounts.adapter = accountListAdapter
        spnrAccounts.setSelection(selectedPosition)
    }

    override fun close() {
        finish()
    }

    companion object {

        fun newIntent(context: Context) =
                Intent(context, NewEmailActivity::class.java)

    }
}
