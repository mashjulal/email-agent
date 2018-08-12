package com.mashjulal.android.emailagent.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.data.repository.mail.stub.AccountRepositoryStub
import com.mashjulal.android.emailagent.data.repository.mail.stub.MailDomainRepositoryStub
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.ui.MessageContentActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.let {
            val id = it.getLongExtra(ARG_ID, -1L)
            mUser = AccountRepositoryStub().getUserById(id)
        }
        initRecyclerView()
        update(0)
    }

    private fun initRecyclerView() {
        recyclerView.adapter = MailBoxRecyclerViewAdapter(mutableListOf()
        ) { messageNumber ->
            startActivity(MessageContentActivity.newIntent(this, mUser, messageNumber))
        }
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
    }

    private fun update(offset: Int) {
        Single.fromCallable {
            val mailDomains = MailDomainRepositoryStub()
            val domains = mailDomains.getByName("yandex")
            val mailRep = DefaultMailRepository(
                    DefaultMailRepository.FOLDER_INBOX,
                    domains.first { it.protocol == "imap" },
                    domains.first { it.protocol == "smtp" }
            )
            mailRep.getMail(mUser, offset)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data ->
                    (recyclerView.adapter as MailBoxRecyclerViewAdapter).onNewData(data)
                }
    }

    companion object {

        private const val ARG_ID = "id"

        fun newIntent(context: Context, userId: Long) : Intent =
                Intent(context, MainActivity::class.java).apply {
                    putExtra(ARG_ID, userId)
                }
    }
}
