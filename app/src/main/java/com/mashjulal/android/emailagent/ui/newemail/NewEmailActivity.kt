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
        val account = spnr_accounts.selectedItem as Account
        val to = et_to.text.toString()
        val subject = et_subject.text.toString()
        val content = et_text.text.toString()
        val subscription = et_subscription.text.toString()
        presenter.sendEmail(account, to, subject, content, subscription)
    }

    override fun setAccounts(accounts: List<Account>, selectedPosition: Int) {
        accountListAdapter = AccountListAdapter(this, accounts)
        spnr_accounts.adapter = accountListAdapter
        spnr_accounts.setSelection(selectedPosition)
    }

    override fun close() {
        finish()
    }

    companion object {

        fun newIntent(context: Context) =
                Intent(context, NewEmailActivity::class.java)

    }
}
