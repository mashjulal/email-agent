package com.mashjulal.android.emailagent.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File

fun showFile(context: Context, file: File, mime: String) {
    val i = Intent()
    i.action = Intent.ACTION_VIEW
    i.setDataAndType(Uri.fromFile(file), mime)
    context.startActivity(i)
}