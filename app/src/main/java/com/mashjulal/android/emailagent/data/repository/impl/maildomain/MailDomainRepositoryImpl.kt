package com.mashjulal.android.emailagent.data.repository.impl.maildomain

import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class MailDomainRepositoryImpl @Inject constructor(
        private val sourceRemote: MailDomainDataStorage,
        private val sourceLocal: MailDomainDataStorage
) : MailDomainRepository {

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Single<MailDomain> {
        return sourceLocal.getByNameAndProtocol(name, protocol)
                .onErrorResumeNext(
                        sourceRemote.getByNameAndProtocol(name, protocol)
                                .doOnSuccess {
                                    val id = sourceLocal.add(it).blockingGet()
                                    Timber.i("Item inserted: $id ${it.name} ${it.protocol}")
                                }
                )
                .doOnError {
                    Timber.e("An error has occurred: $it")
                }
    }
}