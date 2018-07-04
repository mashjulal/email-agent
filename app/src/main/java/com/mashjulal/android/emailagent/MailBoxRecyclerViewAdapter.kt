package com.mashjulal.android.emailagent

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.mashjulal.android.emailagent.objects.mailbox.Message
import kotlinx.android.synthetic.main.item_message.view.*

class MailBoxRecyclerViewAdapter(
        private val mMessages: MutableList<Message>
) : RecyclerView.Adapter<MailBoxRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = mMessages[position]

        holder.rbUnread.isChecked = !message.isRead
        holder.tvSender.text = message.from[0]
        holder.tvSubject.text = message.subject
    }

    override fun getItemCount(): Int {
        return mMessages.size
    }

    fun onNewData(messages: List<Message>) {
        mMessages.clear()
        mMessages.addAll(messages)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvSender: TextView = itemView.tv_sender
        var tvSubject: TextView = itemView.tv_subject
        var rbUnread: RadioButton = itemView.rb_unread
    }
}