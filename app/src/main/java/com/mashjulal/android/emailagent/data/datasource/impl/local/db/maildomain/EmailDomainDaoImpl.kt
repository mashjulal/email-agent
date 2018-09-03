package com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain

import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.Dao
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.base.BaseDaoImpl
import com.mashjulal.android.emailagent.domain.model.Protocol
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@Dao
abstract class EmailDomainDaoImpl: BaseDaoImpl<EmailDomainEntity>(), EmailDomainDao {

    override val TABLE_NAME: String = "email_domain"
    override val COLUMN_NAME_ID: String = "id"

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Single<EmailDomainEntity> {
        val query = SimpleSQLiteQuery(
                "SELECT * FROM $TABLE_NAME WHERE name = ? AND protocol = ?",
                arrayOf(name, protocol.ordinal)
        )
        return execSelectSingle(query).subscribeOn(Schedulers.io())
    }
}