package com.mashjulal.android.emailagent.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.ui.utils.LoadingViewHolder
import com.mashjulal.android.emailagent.ui.utils.NoResultsViewHolder
import com.mashjulal.android.emailagent.ui.utils.createTextIcon
import kotlinx.android.synthetic.main.item_message.view.*
import java.text.SimpleDateFormat
import java.util.*

class MailBoxRecyclerViewAdapter(
        private val context: Context,
        private val mMessages: MutableList<EmailHeader>,
        private val mItemSelectedListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mFooterType = FooterType.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            LoadingViewHolder.TYPE -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading, parent, false)
                return LoadingViewHolder(v)
            }
            NoResultsViewHolder.TYPE -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_no_more_result, parent, false)
                return NoResultsViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_message, parent, false)
                return ViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is ViewHolder) {
            return
        }
        val message = mMessages[position]

        holder.senderIcon.setImageDrawable(createTextIcon(message.from.name, message.from.email))
        holder.tvSender.text = message.from.name
        holder.tvSubject.text = message.subject
        holder.tvReceivedAt.text = SimpleDateFormat(
                context.getString(R.string.template_datetime), Locale.ENGLISH)
                .format(message.receivedDate)
        holder.isSeen.isSelected = !message.isRead
        holder.itemView.setOnClickListener {
            mItemSelectedListener.invoke(message.messageNumber)
        }
    }

    override fun getItemCount(): Int {
        return mMessages.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount-1) {
            return when (mFooterType) {
                FooterType.LOADING -> LoadingViewHolder.TYPE
                FooterType.NO_RESULTS -> NoResultsViewHolder.TYPE
            }
        }
        return super.getItemViewType(position)
    }

    fun addData(messages: List<EmailHeader>) {
        val initialSize = mMessages.size
        mMessages.addAll(messages)
        notifyItemRangeInserted(initialSize, messages.size)
    }

    fun clear() {
        mMessages.clear()
        notifyDataSetChanged()
    }

    fun setFooter(type: FooterType) {
        mFooterType = type
        notifyItemChanged(itemCount)
    }

    enum class FooterType {
        LOADING, NO_RESULTS
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val senderIcon: ImageView = itemView.ivAccount
        var tvSender: TextView = itemView.tvSender
        var tvSubject: TextView = itemView.tvSubject
        var isSeen: ImageView = itemView.ivIsSeen
        var tvReceivedAt: TextView = itemView.tvReceived
    }
}