package com.mashjulal.android.emailagent.objects.mailbox

import javax.mail.Folder

interface MailBox {

    fun getInboxFolder(): Folder
    fun getSentFolder(): Folder
    fun getTrashFolder(): Folder
    fun getSpamFolder(): Folder
    fun getDraftsFolder(): Folder
    fun getFolder(folderName: String): Folder
    fun getAllFolders(): List<Folder>
}