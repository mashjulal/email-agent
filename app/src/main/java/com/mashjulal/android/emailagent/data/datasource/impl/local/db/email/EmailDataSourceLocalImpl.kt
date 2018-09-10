package com.mashjulal.android.emailagent.data.datasource.impl.local.db.email

import com.mashjulal.android.emailagent.data.datasource.api.EmailDataSource
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.EmailMappers
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAddressDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailAttachmentDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.email.dao.EmailDao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder.FolderDao
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class EmailDataSourceLocalImpl @Inject constructor(
        private val folderDao: FolderDao,
        private val emailDao: EmailDao,
        private val emailAddressDao: EmailAddressDao,
        private val emailAttachmentDao: EmailAttachmentDao
) : EmailDataSource {
    override fun search(account: Account, folder: String, query: String): Single<List<EmailHeader>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMail(account: Account, folderName: String)
            : Flowable<List<Email>> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
                .flatMapPublisher{
                    emailDao.getAllByAccountAndFolderOrderByReceiveDateDesc(account.id, it.id)
                }
                .map { it.map { e ->
                    val from = emailAddressDao.getById(e.fromAddressId)
                            .blockingGet()
                    val to = emailAddressDao.getById(e.toAddressId)
                            .blockingGet()
                    val attachments = emailAttachmentDao.getAllByEmailId(e.id)
                            .blockingGet()
                    EmailMappers.toEmailModel(e, from, to, attachments) }
                }

    override fun getMailHeaders(account: Account, folderName: String)
            : Flowable<List<EmailHeader>> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
            .flatMapPublisher{
                emailDao.getAllByAccountAndFolderOrderByReceiveDateDesc(account.id, it.id)
            }
            .map {
                it.map { entity ->
                    val from = emailAddressDao.getById(entity.fromAddressId).blockingGet()
                    val to = emailAddressDao.getById(entity.toAddressId).blockingGet()
                    EmailHeader(entity.messageNumber, entity.subject,
                            EmailAddress(from.email, from.name),
                            EmailAddress(to.email, to.name),
                            Date(entity.receivedDate), entity.isRead)
                }
            }

    override fun getMailByNumber(account: Account, folderName: String, number: Int)
            : Single<Email> = folderDao.getByAccountIdAndFolderName(account.id, folderName)
            .flatMap { emailDao.getByAccountFolderAndMessageNumber(account.id, it.id, number)
            }
            .map {
                val from = emailAddressDao.getById(it.fromAddressId)
                        .blockingGet()
                val to = emailAddressDao.getById(it.toAddressId)
                        .blockingGet()
                val attachments = emailAttachmentDao.getAllByEmailId(it.id)
                        .blockingGet()
                EmailMappers.toEmailModel(it, from, to, attachments) }

    override fun sendMail(account: Account, folderName: String, email: Email): Completable {
        throw UnsupportedOperationException()
    }
}