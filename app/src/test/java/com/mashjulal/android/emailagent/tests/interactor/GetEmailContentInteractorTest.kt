package com.mashjulal.android.emailagent.tests.interactor

import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.impl.GetEmailContentInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.MailDomain
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailContent
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.mail.internet.InternetAddress

@RunWith(MockitoJUnitRunner::class)
class GetEmailContentInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var accountRepository: AccountRepository
    @Mock
    lateinit var emailRepositoryFactory: EmailRepositoryFactory

    @InjectMocks
    lateinit var getEmailContentInteractor: GetEmailContentInteractorImpl

    @Test
    fun test_getContent_Valid() {
        val account = Account(1, "", "address", "123")
        val folder = "Inbox"
        val emailRep = mock(MailRepository::class.java)
        val msgNumber = 1
        val email = Email(
                EmailHeader(msgNumber,
                        "Subject",
                        EmailAddress(InternetAddress("from-email@address.com")),
                        EmailAddress(InternetAddress("to-email@address.com")),
                        Date(1),
                        true),
                EmailContent("Plain Content", "<html>Html Content</html>", listOf())
        )

        doReturn(Single.just(account)).`when`(accountRepository).getUserById(anyLong())
        doReturn(Single.just(emailRep)).`when`(emailRepositoryFactory).createRepository(account, folder)
        doReturn(Single.just(email)).`when`(emailRep).getMailByNumber(anyInt())

        val testSub = getEmailContentInteractor.getContent(account.id, folder, msgNumber).test()

        verify(accountRepository, times(1)).getUserById(anyLong())
        verify(emailRepositoryFactory, times(1)).createRepository(account, folder)
        verify(emailRep, times(1)).getMailByNumber(anyInt())
        testSub
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(email)
    }

    @Test
    fun test_getContent_NoAccount() {
        val folder = "Inbox"
        val msgNumber = 1
        val error = Exception()

        doReturn(Single.error<Account>(error)).`when`(accountRepository).getUserById(anyLong())

        getEmailContentInteractor.getContent(-1L, folder, msgNumber).test()
                .assertTerminated()
                .assertError(error)
                .cancel()
    }

    @Test
    fun test_getContent_NoMessage() {
        val account = Account(1, "", "address", "123")
        val folder = "Inbox"
        val emailRep = mock(MailRepository::class.java)
        val msgNumber = 1
        val error = Exception()

        doReturn(Single.just(account)).`when`(accountRepository).getUserById(anyLong())
        doReturn(Single.just(emailRep)).`when`(emailRepositoryFactory).createRepository(account, folder)
        doReturn(Single.error<MailDomain>(error)).`when`(emailRep).getMailByNumber(anyInt())

        getEmailContentInteractor.getContent(account.id, folder, msgNumber).test()
                .assertTerminated()
                .assertError(error)
                .cancel()
    }
}