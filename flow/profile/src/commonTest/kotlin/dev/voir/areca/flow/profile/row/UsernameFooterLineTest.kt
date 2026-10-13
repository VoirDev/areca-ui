package dev.voir.areca.flow.profile.row

import kotlin.test.Test
import kotlin.test.assertEquals

/** Which line the username footer draws: complaint, then the service's state, then the hint. */
class UsernameFooterLineTest {

    @Test
    fun `a complaint outranks every verdict`() {
        UsernameCheck.entries.forEach { check ->
            assertEquals(
                UsernameFooterLine.Complaint,
                usernameFooterLine(check = check, complaint = "Too short", hint = "Rules"),
            )
        }
    }

    @Test
    fun `checking outranks the hint`() {
        assertEquals(
            UsernameFooterLine.Checking,
            usernameFooterLine(check = UsernameCheck.Checking, complaint = null, hint = "Rules"),
        )
    }

    @Test
    fun `an available name says so`() {
        assertEquals(
            UsernameFooterLine.Available,
            usernameFooterLine(check = UsernameCheck.Available, complaint = null, hint = "Rules"),
        )
    }

    @Test
    fun `a taken name says so`() {
        assertEquals(
            UsernameFooterLine.Taken,
            usernameFooterLine(check = UsernameCheck.Taken, complaint = null, hint = "Rules"),
        )
    }

    @Test
    fun `the hint shows while nothing is known`() {
        assertEquals(
            UsernameFooterLine.Hint,
            usernameFooterLine(check = UsernameCheck.Idle, complaint = null, hint = "Rules"),
        )
    }

    @Test
    fun `nothing is drawn with no hint and nothing known`() {
        assertEquals(
            UsernameFooterLine.None,
            usernameFooterLine(check = UsernameCheck.Idle, complaint = null, hint = null),
        )
    }

    @Test
    fun `only verdicts and complaints are announced`() {
        val announced = UsernameFooterLine.entries.filter { it.isAnswer }.toSet()

        assertEquals(
            setOf(UsernameFooterLine.Complaint, UsernameFooterLine.Available, UsernameFooterLine.Taken),
            announced,
        )
    }
}
