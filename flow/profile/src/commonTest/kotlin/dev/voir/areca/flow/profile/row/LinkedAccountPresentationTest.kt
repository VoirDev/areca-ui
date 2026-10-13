package dev.voir.areca.flow.profile.row

import kotlin.test.Test
import kotlin.test.assertEquals

/** The line and the action of a linked-account row in each of its states. */
class LinkedAccountPresentationTest {

    @Test
    fun `an unlinked provider shows the not linked line and links`() {
        val presentation = LinkedAccountPresentation.of(
            isLinked = false,
            canUnlink = true,
            account = "ada@example.com",
            lockedReason = "Your only way in",
            notLinkedLabel = "Not linked",
        )

        assertEquals(LinkedAccountPresentation("Not linked", LinkedAccountAction.Link), presentation)
    }

    @Test
    fun `a linked provider shows the account and unlinks`() {
        val presentation = LinkedAccountPresentation.of(
            isLinked = true,
            canUnlink = true,
            account = "ada@example.com",
            lockedReason = "Your only way in",
            notLinkedLabel = "Not linked",
        )

        assertEquals(LinkedAccountPresentation("ada@example.com", LinkedAccountAction.Unlink), presentation)
    }

    @Test
    fun `a locked provider shows why and does nothing`() {
        val presentation = LinkedAccountPresentation.of(
            isLinked = true,
            canUnlink = false,
            account = "ada@example.com",
            lockedReason = "Your only way in",
            notLinkedLabel = "Not linked",
        )

        assertEquals(LinkedAccountPresentation("Your only way in", LinkedAccountAction.None), presentation)
    }

    @Test
    fun `a locked provider with no reason falls back to the account`() {
        val presentation = LinkedAccountPresentation.of(
            isLinked = true,
            canUnlink = false,
            account = "ada@example.com",
            lockedReason = null,
            notLinkedLabel = null,
        )

        assertEquals(LinkedAccountPresentation("ada@example.com", LinkedAccountAction.None), presentation)
    }

    @Test
    fun `an unlinked provider is never locked`() {
        val presentation = LinkedAccountPresentation.of(
            isLinked = false,
            canUnlink = false,
            account = null,
            lockedReason = "Your only way in",
            notLinkedLabel = null,
        )

        assertEquals(LinkedAccountPresentation(null, LinkedAccountAction.Link), presentation)
    }
}
