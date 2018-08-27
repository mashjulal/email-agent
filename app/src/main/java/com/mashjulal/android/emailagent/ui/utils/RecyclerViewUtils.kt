package com.mashjulal.android.emailagent.ui.utils

import android.support.v7.widget.RecyclerView
import android.view.View

class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        const val TYPE = -1
    }
}

class NoResultsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    companion object {
        const val TYPE = -2
    }
}