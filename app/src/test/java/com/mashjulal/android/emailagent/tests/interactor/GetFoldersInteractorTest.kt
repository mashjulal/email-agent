package com.mashjulal.android.emailagent.tests.interactor

import android.content.res.Resources
import com.mashjulal.android.emailagent.data.repository.api.AccountRepository
import com.mashjulal.android.emailagent.data.repository.api.FolderRepository
import com.mashjulal.android.emailagent.data.repository.api.PreferenceManager
import com.mashjulal.android.emailagent.data.repository.impl.folder.FolderRepositoryFactory
import com.mashjulal.android.emailagent.domain.interactor.impl.GetFoldersInteractorImpl
import com.mashjulal.android.emailagent.domain.model.Account
import com.mashjulal.android.emailagent.utils.RxImmediateSchedulerRule
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetFoldersInteractorTest {

    @Rule
    @JvmField
    val scheduleRule: TestRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var resources: Resources
    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var preferenceManager: PreferenceManager

    @Mock
    lateinit var folderRepositoryFactory: FolderRepositoryFactory

    @InjectMocks
    lateinit var getFoldersInteractor: GetFoldersInteractorImpl

    @Test
    fun test_getFoldersWithCurrent_Valid() {
        val account = Account(1, "", "address", "123")
        val accountId = 1L
        val defFolders = arrayOf("Inbox", "Sent", "Trash", "Spam")
        val folders = listOf("Inbox", "Sent", "Trash", "Spam", "Outbox")
        val folder = "INBOX"
        val folderPos = 0
        val folderRep = Mockito.mock(FolderRepository::class.java)

        doReturn(Single.just(accountId)).`when`(preferenceManager).getLastSelectedUserId()
        doReturn(defFolders).`when`(resources).getStringArray(anyInt())
        doReturn(Single.just(account)).`when`(accountRepository).getUserById(anyLong())
        doReturn(Single.just(folderRep)).`when`(folderRepositoryFactory).createFolderRepository(account)
        doReturn(Single.just(folders)).`when`(folderRep).getAll(account)

        val expected = Triple(folders, folder, folderPos)

        val testSub =
                getFoldersInteractor.getFoldersWithCurrent(folder).test()

        verify(preferenceManager, times(1)).getLastSelectedUserId()
        verify(resources, times(1)).getStringArray(anyInt())
        verify(accountRepository, times(1)).getUserById(anyLong())
        verify(folderRepositoryFactory, times(1)).createFolderRepository(account)
        verify(folderRep, times(1)).getAll(account)

        testSub
                .assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(expected)
    }
}