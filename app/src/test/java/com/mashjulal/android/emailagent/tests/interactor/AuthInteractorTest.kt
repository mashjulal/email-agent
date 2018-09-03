package com.mashjulal.android.emailagent.tests.interactor

import com.mashjulal.android.emailagent.data.datasource.impl.remote.StoreUtils
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailDomainRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.domain.interactor.impl.AuthInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.Protocol
import com.mashjulal.android.emailagent.utils.EmailUtils
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var accountRepository: AccountRepository
    @Mock
    lateinit var mailDomainRepository: MailDomainRepository
    @Mock
    lateinit var preferenceManager: PreferenceManager
    @Mock
    lateinit var emailUtils: EmailUtils
    @Mock
    lateinit var storeUtils: StoreUtils

    @InjectMocks
    lateinit var authInteractor: AuthInteractorImpl

    @Test
    fun test_auth_success() {
        val address = "example@example.com"
        val password = "123"
        val account = Account(0, "", address, password)
        val protocol = Protocol.IMAP
        val domain = MailDomain(0, "example", protocol, "localhost", 123)

        doReturn(domain.name).`when`(emailUtils).getDomainFromEmail(address)
        doReturn(Single.just(domain)).`when`(mailDomainRepository).getByNameAndProtocol(domain.name, protocol)
        doReturn(Completable.complete()).`when`(storeUtils).auth(domain, account)
        doReturn(Single.just(1L)).`when`(accountRepository).addUser(account)
        doReturn(Completable.complete()).`when`(preferenceManager).setLastSelectedUserId(anyLong())

        val testSub = authInteractor.auth(account).test()

        verify(emailUtils, times(1)).getDomainFromEmail(address)
        verify(mailDomainRepository, times(1)).getByNameAndProtocol(domain.name, protocol)
        verify(storeUtils, times(1)).auth(domain, account)
        verify(accountRepository, times(1)).addUser(account)
        verify(preferenceManager, times(1)).setLastSelectedUserId(anyLong())

        testSub
                .assertComplete()
                .assertNoErrors()
    }
}