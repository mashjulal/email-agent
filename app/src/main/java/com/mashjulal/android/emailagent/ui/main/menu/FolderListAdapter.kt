package com.mashjulal.android.emailagent.ui.main.menu

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import kotlinx.android.synthetic.main.item_folder.view.*

class FolderListAdapter(
        private val folders: List<String>,
        private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(v)
    }

    override fun getItemCount(): Int = folders.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]

        holder.title.text = folder
        holder.itemView.isSelected = selectedPosition == position
    }

    fun setSelected(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(selectedPosition)
    }

    fun getSelected(): String {
        return folders[selectedPosition]
    }

    inner class FolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView = itemView.tvTitle

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION)
                return
            setSelected(adapterPosition)
            onItemClickListener.invoke(selectedPosition)
        }
    }


}