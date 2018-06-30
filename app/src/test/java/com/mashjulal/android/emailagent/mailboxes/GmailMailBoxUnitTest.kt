package com.mashjulal.android.emailagent.mailboxes

import com.mashjulal.android.emailagent.objects.mailbox.GmailMailBox
import com.mashjulal.android.emailagent.objects.user.User
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream
import java.util.*

class GmailMailBoxUnitTest {

    private val props = Properties()
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var mailbox: GmailMailBox

    @Before
    fun setup() {
        props.load(FileInputStream(
                "src/test/resources/gmail-config.properties"))
        username = props["email"].toString()
        password = props["password"].toString()
        mailbox = GmailMailBox(User("", username, password))
    }

    @After
    fun tearDown() {
        props.clear()
        username = ""
        password = ""
    }

    @Test
    fun mailbox_hasInbox() {
        val folder = mailbox.getInboxFolder()

        val expected = "INBOX".toLowerCase()
        val actual = folder.name.toLowerCase()

        assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasSent() {
        val folder = mailbox.getSentFolder()

        val expected = "Отправленные".toLowerCase()
        val actual = mailbox.getSentFolder().name.toLowerCase()

        assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasSpam() {
        val folder = mailbox.getInboxFolder()

        val expected = "Спам".toLowerCase()
        val actual = mailbox.getSpamFolder().name.toLowerCase()

        assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasDrafts() {
        val folder = mailbox.getInboxFolder()

        val expected = "Черновики".toLowerCase()
        val actual = mailbox.getDraftsFolder().name.toLowerCase()

        assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasTrash() {
        val folder = mailbox.getInboxFolder()

        val expected = "Корзина".toLowerCase()
        val actual = mailbox.getTrashFolder().name.toLowerCase()

        assertNotNull(folder)
        assertEquals(expected, actual)
    }
}
