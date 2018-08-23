package com.mashjulal.android.emailagent.data.repository.mail

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.domain.repository.MailDomainRepository
import javax.inject.Inject

class MailDomainRepositoryImpl @Inject constructor(
        private val dbReference: DatabaseReference
) : MailDomainRepository {

    override fun getByNameAndProtocol(name: String, protocol: Protocol): MailDomain {
        val tcs = TaskCompletionSource<MailDomain>()

        dbReference.child("${name}_${protocol.name.toLowerCase()}")
                .addListenerForSingleValueEvent(object: ValueEventListener {

                    override fun onCancelled(err: DatabaseError) {
                        tcs.setException(err.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val host = snapshot.child("host").value as String
                        val port = snapshot.child("port").value as Long
                        tcs.setResult(MailDomain(name, protocol, host, port.toInt()))
                    }

                })
        val task = tcs.task
        Tasks.await(task)
        return task.result
    }
}