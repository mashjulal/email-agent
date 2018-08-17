package com.mashjulal.android.emailagent.ui.messagecontent

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Attachment
import kotlinx.android.synthetic.main.item_attachment.view.*

class AttachmentListAdapter(
        private val data: MutableList<Attachment>,
        private val onItemClickListener: (Int) -> Unit
): RecyclerView.Adapter<AttachmentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_attachment, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.fileName.text = item.filename
        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(position)
        }
    }

    fun onNewData(newData: List<Attachment>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.tv_name
    }
}