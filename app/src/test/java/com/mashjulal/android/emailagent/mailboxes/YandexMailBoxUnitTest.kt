package com.mashjulal.android.emailagent.mailboxes

import com.mashjulal.android.emailagent.objects.mailbox.YandexMailBox
import com.mashjulal.android.emailagent.objects.user.User
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream
import java.util.*

class YandexMailBoxUnitTest {

    private val props = Properties()
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var mailbox: YandexMailBox

    @Before
    fun setup() {
        props.load(FileInputStream(
                "src/test/resources/yandex-mail-config.properties"))
        username = props["email"].toString()
        password = props["password"].toString()
        mailbox = YandexMailBox(User("", username, password))
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

        Assert.assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasSent() {
        val folder = mailbox.getSentFolder()

        val expected = "SENT".toLowerCase()
        val actual = mailbox.getSentFolder().name.toLowerCase()

        Assert.assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasSpam() {
        val folder = mailbox.getInboxFolder()

        val expected = "SPAM".toLowerCase()
        val actual = mailbox.getSpamFolder().name.toLowerCase()

        Assert.assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasDrafts() {
        val folder = mailbox.getInboxFolder()

        val expected = "DRAFTS".toLowerCase()
        val actual = mailbox.getDraftsFolder().name.toLowerCase()

        Assert.assertNotNull(folder)
        assertEquals(expected, actual)
    }

    @Test
    fun mailbox_hasTrash() {
        val folder = mailbox.getInboxFolder()

        val expected = "TRASH".toLowerCase()
        val actual = mailbox.getTrashFolder().name.toLowerCase()

        Assert.assertNotNull(folder)
        assertEquals(expected, actual)
    }
}
