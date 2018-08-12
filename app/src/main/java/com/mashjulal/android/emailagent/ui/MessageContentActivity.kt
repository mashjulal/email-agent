package com.mashjulal.android.emailagent.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.text.util.Linkify
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.data.repository.mail.stub.AccountRepositoryStub
import com.mashjulal.android.emailagent.data.repository.mail.stub.MailDomainRepositoryStub
import com.mashjulal.android.emailagent.domain.model.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_content.*
import net.nightwhistler.htmlspanner.HtmlSpanner
import net.nightwhistler.htmlspanner.handlers.ImageHandler
import org.htmlcleaner.TagNode

class MessageContentActivity : AppCompatActivity() {

    private lateinit var user: User
    private var messageNumber: Int = 0

    private val imageHandler = object: ImageHandler() {
        override fun handleTagNode(node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int) {
            val src = node.getAttributeByName("src")
            builder.append("￼")
            val bitmap = this.loadBitmap(src)
            if (bitmap != null) {
                val drawable = BitmapDrawable(resources, bitmap)
                drawable.setBounds(0, 0, bitmap.width - 1, bitmap.height - 1)
                builder.setSpan(ImageSpan(drawable), start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_content)

        intent.let {
            messageNumber = intent.getIntExtra(ARG_MESSAGE_NUMBER, -1)

            val id = it.getLongExtra(ARG_ID, -1L)
            user = AccountRepositoryStub().getUserById(id)
        }
        parseMessageContent()
    }

    private fun parseMessageContent() {
        getMessageByNumber(messageNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    val (subject, views) = it
                    supportActionBar.let { title = subject }
                    val params = LinearLayout.LayoutParams(container.layoutParams).apply {
                        setMargins(64, 64, 64, 64)
                    }
                    views.forEach { container.addView(it, params)} }
    }

    private fun getMessageByNumber(number: Int): Single<Pair<String, List<View>>> =
        Single.fromCallable {
            val mailDomains = MailDomainRepositoryStub()
            val domains = mailDomains.getByName("yandex")
            val mailRep = DefaultMailRepository(
                    DefaultMailRepository.FOLDER_INBOX,
                    domains.first { it.protocol == "imap" },
                    domains.first { it.protocol == "smtp" }
            )
            val message = mailRep.getMailByNumber(user, number)

            val subject = message.subject
            val views = mutableListOf<View>()
            val htmlSpanner = HtmlSpanner()
            htmlSpanner.registerHandler("img", imageHandler)

            val messageContent = message.content
            for (part in messageContent) {
                if (part.mimeType == "text/html") {
                    views.add(TextView(this).apply {
                        autoLinkMask = Linkify.ALL
                        linksClickable = true
                        movementMethod = LinkMovementMethod()
                        text = htmlSpanner.fromHtml(part.content)
                    })
                }
            }
            subject to views
        }

    companion object {

        private const val ARG_ID = "id"
        private const val ARG_MESSAGE_NUMBER = "message_number"

        fun newIntent(context: Context, user: User, messageNumber: Int) =
                Intent(context, MessageContentActivity::class.java).apply {
                    putExtra(ARG_ID, user.id)
                    putExtra(ARG_MESSAGE_NUMBER, messageNumber)
                }
    }
}
