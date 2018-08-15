package com.mashjulal.android.emailagent.ui.messagecontent

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.text.util.Linkify
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.User
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_content.*
import net.nightwhistler.htmlspanner.HtmlSpanner
import net.nightwhistler.htmlspanner.handlers.ImageHandler
import org.htmlcleaner.TagNode
import javax.inject.Inject

class MessageContentActivity : DaggerAppCompatActivity(), MessageContentView {

    @Inject
    lateinit var presenter: MessageContentPresenter

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
            val messageNumber = intent.getIntExtra(ARG_MESSAGE_NUMBER, -1)
            val id = it.getLongExtra(ARG_ID, -1L)
            presenter.requestMessageContent(id, messageNumber)
        }
    }


    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun showMessageTitle(subject: String) {
        supportActionBar.let { title = subject }
    }

    override fun showMessageContent(content: List<String>) {
        Observable.create<View> {
            val htmlSpanner = HtmlSpanner()
            htmlSpanner.registerHandler("img", imageHandler)

            val params = LinearLayout.LayoutParams(container.layoutParams).apply {
                setMargins(64, 64, 64, 64)
            }
            for (html in content) {
                val v = TextView(this).apply {
                    autoLinkMask = Linkify.ALL
                    linksClickable = true
                    movementMethod = LinkMovementMethod()
                    text = htmlSpanner.fromHtml(html)
                }
                v.layoutParams = params
                it.onNext(v)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    container.addView(it)
                }
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
