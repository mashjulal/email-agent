package com.mashjulal.android.emailagent.ui.newemail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mashjulal.android.emailagent.R
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.ui.utils.createTextIcon
import kotlinx.android.synthetic.main.item_spinner_account.view.*

class AccountListAdapter(
        context: Context,
        private val accounts: List<Account>
) : ArrayAdapter<Account>(context, R.layout.item_spinner_account, accounts) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val account = accounts[position]
        val v = convertView ?: inflater.inflate(R.layout.item_spinner_account, parent, false)

        v.iv_account.setImageDrawable(
                    createTextIcon(account.address[0].toString(), account.address))
        v.tv_address.text = account.address
        return v
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}