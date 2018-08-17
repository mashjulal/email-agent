package com.mashjulal.android.emailagent.ui.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.InputStream

fun File.copyInputStreamToFile(input: InputStream) {
    input.use {
        this.outputStream().use {
            input.copyTo(it)
        }
    }
}

fun saveToAppFiles(context: Context, filename: String, input: InputStream): File {
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
    if (!file.exists()) {
        file.copyInputStreamToFile(input)
    }
    return file
}