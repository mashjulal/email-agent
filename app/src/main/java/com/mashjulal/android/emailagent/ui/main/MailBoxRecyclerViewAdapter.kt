package com.mashjulal.android.emailagent.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.EmailHeader
import com.mashjulal.android.emailagent.ui.utils.createTextIcon
import kotlinx.android.synthetic.main.item_message.view.*

class MailBoxRecyclerViewAdapter(
        private val mMessages: MutableList<EmailHeader>,
        private val mItemSelectedListener: (Int) -> Unit
) : RecyclerView.Adapter<MailBoxRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = mMessages[position]

        holder.senderIcon.setImageDrawable(createTextIcon(message.from.name, message.from.email))
        holder.tvSender.text = message.from.name
        holder.tvSubject.text = message.subject
        holder.itemView.setOnClickListener {
            mItemSelectedListener.invoke(message.messageNumber)
        }
    }

    override fun getItemCount(): Int {
        return mMessages.size
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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val senderIcon: ImageView = itemView.iv_senderIcon
        var tvSender: TextView = itemView.tv_sender
        var tvSubject: TextView = itemView.tv_subject
    }
}