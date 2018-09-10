package com.mashjulal.android.emailagent.tests.utils.mappers

import com.mashjulal.android.emailagent.data.datasource.impl.local.db.EmailDomainMappers
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.maildomain.EmailDomainEntity
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EmailDomainMappersTest {

    @Test
    fun test_toEmailDomainModel_Valid() {
        val entity = EmailDomainEntity(1, "local", "localhost", 123,
                Protocol.IMAP, false)

        val expected = MailDomain(1, "local", Protocol.IMAP, "localhost", 123,
                false)
        val actual = EmailDomainMappers.toEmailDomainModel(entity)

        assertEquals(expected, actual)
    }

    @Test
    fun test_toEmailDomainEntity_Valid() {
        val model = MailDomain(1, "local", Protocol.IMAP, "localhost", 123,
                false)

        val expected = EmailDomainEntity(1, "local", "localhost", 123,
                Protocol.IMAP, false)
        val actual = EmailDomainMappers.toEmailDomainEntity(model)

        assertEquals(expected, actual)
    }
}