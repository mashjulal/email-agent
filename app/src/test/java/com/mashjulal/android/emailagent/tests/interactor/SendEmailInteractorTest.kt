package com.mashjulal.android.emailagent.tests.interactor

import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.MailRepository
import com.mashjulal.android.emailagent.data.repository.impl.mail.EmailRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.impl.SendEmailInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.domain.model.Folder
import com.mashjulal.android.emailagent.domain.model.email.Email
import com.mashjulal.android.emailagent.domain.model.email.EmailAddress
import com.mashjulal.android.emailagent.domain.model.email.EmailContent
import com.mashjulal.android.emailagent.domain.model.email.EmailHeader
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.mail.internet.InternetAddress

@RunWith(MockitoJUnitRunner::class)
class SendEmailInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var accountRepository: AccountRepository
    @Mock
    lateinit var emailRepositoryFactory: EmailRepositoryFactory

    @InjectMocks
    lateinit var sendEmailInteractor: SendEmailInteractorImpl

    @Test
    fun test_sendEmail_Valid() {
        val emailRep = Mockito.mock(MailRepository::class.java)
        val account = Account(1, "", "address", "123")
        val folder = Folder.SENT.name
        val to = "to-email@address.com"
        val subject = "Subject"
        val text = "Text"
        val subscription = "Email Subscription"
        val email = Email(
                EmailHeader(0,
                        subject,
                        EmailAddress(InternetAddress(account.address)),
                        EmailAddress(InternetAddress(to)),
                        Date(),
                        false),
                EmailContent("$text\n$subscription", "", listOf())
        )

        doReturn(Single.just(emailRep)).`when`(emailRepositoryFactory).createRepository(account, folder)
        doReturn(Completable.complete()).`when`(emailRep).sendMail(email)

        val testSub = sendEmailInteractor
                .sendEmail(account, email).test()

        verify(emailRepositoryFactory, times(1)).createRepository(account, folder)
        verify(emailRep, times(1)).sendMail(email)

        testSub
                .assertComplete()
                .assertNoErrors()
                .assertNoValues()
    }
}