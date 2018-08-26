package com.mashjulal.android.emailagent.ui.messagecontent

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.data.repository.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.repository.AccountRepository
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class MessageContentPresenter @Inject constructor(
        private val mailDomainRepository: MailDomainRepository,
        private val accountRepository: AccountRepository,
        private val emailRepositoryFactory: EmailRepositoryFactory
): BasePresenter<MessageContentView>() {

    fun requestMessageContent(userId: Long, messageNumber: Int) {
        accountRepository.getUserById(userId)
                .flatMap { emailRepositoryFactory.createRepository(it, Folder.INBOX.name) }
                .flatMap {it.getMailByNumber(messageNumber) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { email ->
                    viewState.showMessageTitle(email.emailHeader.subject)
                    viewState.showMessageContent(email.content)
                }.addToComposite(compositeDisposable)
    }
}