package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email

import android.webkit.MimeTypeMap
import com.mashjulal.android.emailagent.data.datasource.api.EmailDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAddressDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAttachmentDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.entity.EmailEntity
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderDao
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.io.File
import java.util.*
import javax.inject.Inject

class EmailDataSourceLocalImpl @Inject constructor(
        private val folderDao: FolderDao,
        private val emailDao: EmailDao,
        private val emailAddressDao: EmailAddressDao,
        private val emailAttachmentDao: EmailAttachmentDao
) : EmailDataSource {

    override fun getMail(account: Account, folderName: String)
            : Flowable<List<Email>> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
                .flatMapPublisher{
                    emailDao.getAllByAccountAndFolderOrderByReceiveDateDesc(account.id, it.id)
                }
                .map { it.map { e -> entityToModel(e)} }

    override fun getMailHeaders(account: Account, folderName: String)
            : Flowable<List<EmailHeader>> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
            .flatMapPublisher{
                emailDao.getAllByAccountAndFolderOrderByReceiveDateDesc(account.id, it.id)
            }
            .map {
                it.map { entity ->
                    val from = emailAddressDao.getById(entity.fromAddressId).blockingGet()
                    EmailHeader(entity.messageNumber, entity.subject,
                            EmailAddress(from.email, from.name),
                            Date(entity.receivedDate), entity.isRead)
                }
            }

    override fun getMailByNumber(account: Account, folderName: String, number: Int)
            : Maybe<Email> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
            .flatMapMaybe {
                emailDao.getByAccountFolderAndMessageNumber(account.id, it.id, number)
            }
            .map { entityToModel(it) }

    private fun entityToModel(emailEntity: EmailEntity): Email {
        val from = emailAddressDao.getById(emailEntity.fromAddressId).blockingGet()
        val attachments = emailAttachmentDao.getAllByEmailId(emailEntity.id).blockingGet()
        val header = EmailHeader(emailEntity.messageNumber, emailEntity.subject,
                EmailAddress(from.email, from.name),
                Date(emailEntity.receivedDate), emailEntity.isRead)
        val attch = attachments.map { att ->
            val ext = MimeTypeMap.getFileExtensionFromUrl(att.path)
            val contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            EmailAttachment(att.path, contentType, File(att.path).inputStream())
        }
        val content = EmailContent(emailEntity.textContent, emailEntity.htmlContent, attch)
        return Email(header, content)
    }
}