package com.mashjulal.android.emailagent.ui.messagecontent

import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.domain.model.EmailContent
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.ui.base.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessageContentPresenter @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val accountRepository: AccountRepository
): BasePresenter<MessageContentView>() {

    fun requestMessageContent(userId: Long, messageNumber: Int) {
        getMessageByNumber(userId, messageNumber)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (subject, content) ->
                    view?.showMessageTitle(subject)
                    view?.showMessageContent(content)
                }
    }

    private fun getMessageByNumber(userId: Long, number: Int): Single<Pair<String, EmailContent>> =
            Single.fromCallable {
                val user = accountRepository.getUserById(userId)
                val domain = user.address.substringAfter("@").substringBefore(".")

                val mailRep = DefaultMailRepository(
                        Folder.INBOX.name,
                        mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP),
                        mailDomainRepository.getByNameAndProtocol(domain, Protocol.SMTP)
                )
                val message = mailRep.getMailByNumber(user, number)
                val subject = message.emailHeader.subject
                val content = message.content

                subject to content
            }
}

interface MessageContentView: MvpView {
    fun showMessageTitle(subject: String)
    fun showMessageContent(content: EmailContent)
}