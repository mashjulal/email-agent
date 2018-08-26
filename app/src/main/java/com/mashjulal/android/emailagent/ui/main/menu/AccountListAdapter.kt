package com.mashjulal.android.emailagent.ui.main.menu

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.utils.createTextIcon
import kotlinx.android.synthetic.main.item_account.view.*

class AccountListAdapter(
        private val accounts: List<Account>,
        private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<AccountListAdapter.UserViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return UserViewHolder(v)
    }

    override fun getItemCount(): Int = accounts.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val account = accounts[position]

        holder.icon.setImageDrawable(createTextIcon(account.address[0].toString(), account.address))
        holder.account.text = account.address
        holder.itemView.isSelected = selectedPosition == position
    }

    fun setSelected(position: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = position
        notifyItemChanged(selectedPosition)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val icon: ImageView = itemView.iv_senderIcon
        val account: TextView = itemView.tv_account
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