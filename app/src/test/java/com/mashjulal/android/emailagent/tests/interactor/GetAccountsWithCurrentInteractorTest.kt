package com.mashjulal.android.emailagent.tests.interactor

import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.domain.interactor.impl.GetAccountsWithCurrentInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAccountsWithCurrentInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var preferenceManager: PreferenceManager
    @Mock
    lateinit var accountRepository: AccountRepository

    @InjectMocks
    lateinit var getAccountsWithCurrentInteractor: GetAccountsWithCurrentInteractorImpl

    @Test
    fun test_get_Valid() {
        val accounts = listOf(
                Account(1, "", "address1", "123"),
                Account(2, "", "address2", "123"),
                Account(3, "", "address3", "123")
        )
        val selectedAccountId = 1L
        val pos = 0

        doReturn(Single.just(accounts)).`when`(accountRepository).getAll()
        doReturn(Single.just(selectedAccountId)).`when`(preferenceManager).getLastSelectedUserId()

        val expected = Triple(accounts, accounts[pos], pos)
        val testSub = getAccountsWithCurrentInteractor.get().test()

        verify(accountRepository, times(1)).getAll()
        verify(preferenceManager, times(1)).getLastSelectedUserId()
        testSub
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(expected)
    }
}