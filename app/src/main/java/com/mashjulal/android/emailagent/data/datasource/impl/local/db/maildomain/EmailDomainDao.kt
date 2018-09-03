package com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain

import com.mashjulal.android.emailagent.data.datasource.impl.local.db.base.BaseDao
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single

interface EmailDomainDao: BaseDao<EmailDomainEntity> {
    fun getByNameAndProtocol(name: String, protocol: Protocol): Single<EmailDomainEntity>
}