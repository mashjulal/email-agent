package com.mashjulal.android.emailagent.tests.interactor

import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.impl.GetEmailHeadersInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.mail.internet.InternetAddress

@RunWith(MockitoJUnitRunner::class)
class GetEmailHeadersInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var emailRepositoryFactory: EmailRepositoryFactory

    @InjectMocks
    lateinit var getEmailHeadersInteractor: GetEmailHeadersInteractorImpl

    @Test
    fun test_getHeaders_Valid() {
        val account = Account(1, "", "address", "123")
        val folder = "Inbox"
        val emailRep = mock(MailRepository::class.java)
        val emailHeaders = listOf(
                EmailHeader(1, "Subject1",
                        EmailAddress(InternetAddress("from-email@address.com")),
                        EmailAddress(InternetAddress("to-email@address.com")),
                        Date(1), true),
                EmailHeader(2, "Subject2",
                        EmailAddress(InternetAddress("from-email@address.com")),
                        EmailAddress(InternetAddress("to-email@address.com")),
                        Date(10), false)
        )

        doReturn(Single.just(emailRep)).`when`(emailRepositoryFactory).createRepository(account, folder)
        doReturn(Flowable.just(emailHeaders)).`when`(emailRep).getMailHeaders()

        val testSub = getEmailHeadersInteractor
                .getHeaders(account, folder, 0).test()

        verify(emailRepositoryFactory, times(1)).createRepository(account, folder)
        verify(emailRep, times(1)).getMailHeaders()
        testSub
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(emailHeaders)
                .cancel()
    }

    @Test
    fun test_getHeaders_NoDomain() {
        val account = Account(1, "", "address", "123")
        val folder = "Inbox"
        val error = Exception()

        doReturn(Single.error<MailDomain>(error)).`when`(emailRepositoryFactory).createRepository(account, folder)

        val testSub = getEmailHeadersInteractor
                .getHeaders(account, folder, 0).test()

        verify(emailRepositoryFactory, times(1)).createRepository(account, folder)
        testSub
                .assertTerminated()
                .assertError(error)
                .cancel()
    }
}