package com.mashjulal.android.emailagent.tests.utils.mappers

import com.mashjulal.android.emailagent.data.datasource.impl.local.db.AccountMappers
import com.mashjulal.android.emailagent.data.datasource.impl.local.db.account.entity.AccountEntity
import com.mashjulal.android.emailagent.domain.model.Account
import junit.framework.TestCase.assertEquals
import org.junit.Test

class AccountMappersTest {

    @Test
    fun test_toAccountDomainModel_Valid() {
        val entity = AccountEntity(1, "local", "localhost@host", "123")

        val expected = Account(1, "local", "localhost@host", "123")
        val actual = AccountMappers.toAccountModel(entity)

        assertEquals(expected, actual)
    }

    @Test
    fun test_toAccountDomainEntity_Valid() {
        val model = Account(1, "local", "localhost@host", "123")

        val expected = AccountEntity(1, "local", "localhost@host", "123")
        val actual = AccountMappers.toAccountEntity(model)

        assertEquals(expected, actual)
    }
}