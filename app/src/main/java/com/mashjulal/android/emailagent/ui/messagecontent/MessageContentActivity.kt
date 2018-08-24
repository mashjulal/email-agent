package com.mashjulal.android.emailagent.ui.messagecontent

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Attachment
import com.mashjulal.android.emailagent.domain.model.EmailContent
import com.mashjulal.android.emailagent.domain.model.User
import com.mashjulal.android.emailagent.ui.base.BaseActivity
import com.mashjulal.android.emailagent.ui.utils.showFile
import com.mashjulal.android.emailagent.utils.saveToAppFiles
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_content.*
import net.nightwhistler.htmlspanner.HtmlSpanner
import net.nightwhistler.htmlspanner.handlers.ImageHandler
import org.htmlcleaner.TagNode
import javax.inject.Inject


class MessageContentActivity : BaseActivity(), MessageContentView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MessageContentPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private lateinit var attachmentAdapter: AttachmentListAdapter
    private lateinit var attachments: List<Attachment>

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

        initAttachmentList()
        intent.let {
            val messageNumber = intent.getIntExtra(ARG_MESSAGE_NUMBER, -1)
            val id = it.getLongExtra(ARG_ID, -1L)
            presenter.requestMessageContent(id, messageNumber)
        }
    }

    private fun initAttachmentList() {
        attachmentAdapter = AttachmentListAdapter(mutableListOf()) {position ->
            val attachment = attachments[position]
            val attachmentFile = saveToAppFiles(this, attachment.filename, attachment.inputStream)
            showFile(this, attachmentFile, attachment.contentType)
        }
        rv_attachments.adapter = attachmentAdapter
    }

    override fun showMessageTitle(subject: String) {
        supportActionBar.let { title = subject }
    }

    override fun showMessageContent(content: EmailContent) {
        Single.fromCallable {
            val htmlSpanner = HtmlSpanner()
            htmlSpanner.registerHandler("img", imageHandler)
            htmlSpanner.fromHtml(content.htmlContent)
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { html ->
                    tv_htmlContent.text = html
                }
        attachments = content.attachments
        attachmentAdapter.onNewData(attachments)
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
