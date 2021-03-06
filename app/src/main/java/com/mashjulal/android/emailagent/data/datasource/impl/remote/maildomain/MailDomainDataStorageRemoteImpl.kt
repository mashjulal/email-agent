package com.mashjulal.android.emailagent.data.datasource.impl.remote.maildomain

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mashjulal.android.emailagent.data.datasource.api.MailDomainDataStorage
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Single
import javax.inject.Inject

class MailDomainDataStorageRemoteImpl @Inject constructor(
        private val dbReference: DatabaseReference
) : MailDomainDataStorage {

    override fun add(mailDomain: MailDomain): Single<Long> {
        return Single.error(NotImplementedError())
    }

    override fun getByNameAndProtocol(name: String, protocol: Protocol): Single<MailDomain> {
        return {
            val tcs = TaskCompletionSource<MailDomain>()

            dbReference.child("${name}_${protocol.name.toLowerCase()}")
                    .addListenerForSingleValueEvent(object: ValueEventListener {

                        override fun onCancelled(err: DatabaseError) {
                            tcs.setException(err.toException())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val host = snapshot.child("host").value as String
                            val port = snapshot.child("port").value as Long
                            val auth = snapshot.child("auth").value as Boolean
                            tcs.setResult(MailDomain(0, name, protocol, host, port.toInt(), auth))
                        }

                    })
            val task = tcs.task
            Tasks.await(task)
            task.result
        }.toIoSingle()
    }
}