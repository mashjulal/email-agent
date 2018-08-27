package com.mashjulal.android.emailagent.ui.messagecontent

import com.arellomobile.mvp.InjectViewState
import com.mashjulal.android.emailagent.domain.interactor.GetEmailContentInteractor
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.ui.base.BasePresenter
import com.mashjulal.android.emailagent.utils.addToComposite
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class MessageContentPresenter @Inject constructor(
        private val getEmailContentInteractor: GetEmailContentInteractor
): BasePresenter<MessageContentView>() {

    fun requestMessageContent(userId: Long, messageNumber: Int) {
        getEmailContentInteractor.getContent(userId, Folder.INBOX.name, messageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { email ->
                    viewState.showMessageTitle(email.emailHeader.subject)
                    viewState.showMessageContent(email.content)
                }.addToComposite(compositeDisposable)
    }
}