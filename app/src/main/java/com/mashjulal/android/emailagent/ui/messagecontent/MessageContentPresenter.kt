package com.mashjulal.android.emailagent.ui.messagecontent

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.data.repository.mail.DefaultMailRepository
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import com.mashjulal.android.emailagent.utils.getDomainFromEmail
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class MessageContentPresenter @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val accountRepository: AccountRepository
): BasePresenter<MessageContentView>() {

    fun requestMessageContent(userId: Long, messageNumber: Int) {
        accountRepository.getUserById(userId)
                .flatMapSingle { user -> Single.fromCallable {
                    val domain = getDomainFromEmail(user.address)

                    val mailRep = DefaultMailRepository(
                            Folder.INBOX.name,
                            mailDomainRepository.getByNameAndProtocol(domain, Protocol.IMAP),
                            mailDomainRepository.getByNameAndProtocol(domain, Protocol.SMTP)
                    )
                    val message = mailRep.getMailByNumber(user, messageNumber)
                    val subject = message.emailHeader.subject
                    val content = message.content

                    subject to content
                } }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (subject, content) ->
                    viewState.showMessageTitle(subject)
                    viewState.showMessageContent(content)
                }.addToComposite(compositeDisposable)
    }
}