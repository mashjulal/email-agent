package com.mashjulal.android.emailagent.tests.utils

import com.mashjulal.android.emailagent.utils.EmailUtils.getDomainFromEmail
import junit.framework.TestCase.assertEquals
import org.junit.Test

class EmailUtils {



    @Test
    fun test_getDomainFromEmail_Valid() {
        val address = "ex@ex.com"

        val expected = "ex"
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_NoAt() {
        val address = "exex.com"

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_NoDot() {
        val address = "ex@excom"

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_NoAtAndDot() {
        val address = "exexcom"

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_NoAddress() {
        val address = "@ex.com"

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_NoDomain() {
        val address = "ex"

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getDomainFromEmail_Empty() {
        val address = ""

        val expected = ""
        val actual = getDomainFromEmail(address)

        assertEquals(expected, actual)
    }
}