package dev.voir.areca.playground.shell

import dev.voir.areca.playground.registry.ShowcaseEntry
import dev.voir.areca.playground.registry.ShowcaseGroup
import dev.voir.areca.playground.registry.ShowcaseRegistry
import dev.voir.areca.playground.registry.ShowcaseSection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SidebarTreeTest {

    private val entries = listOf(
        page("flow-profile/sheet", ShowcaseGroup.ProfileFlow, "Sign out sheet", ShowcaseSection.ProfileSheets),
        page("foundation/text-field", ShowcaseGroup.Components, "Text field", ShowcaseSection.Inputs),
        page("foundation/icon-button", ShowcaseGroup.Components, "icon button", ShowcaseSection.Actions),
        page("foundation/button", ShowcaseGroup.Components, "Button", ShowcaseSection.Actions),
        page("flow-profile/profile", ShowcaseGroup.ProfileFlow, "Profile", ShowcaseSection.ProfileScreens),
        page("foundation/colors", ShowcaseGroup.Foundations, "Colors"),
    )

    @Test
    fun `tree orders groups and sections by declaration and pages by title, ignoring case`() {
        val tree = sidebarTree(entries)

        assertEquals(
            listOf(ShowcaseGroup.Foundations, ShowcaseGroup.Components, ShowcaseGroup.ProfileFlow),
            tree.map { it.group },
        )
        assertEquals(
            listOf(ShowcaseSection.Actions, ShowcaseSection.Inputs),
            tree[1].sections.map { it.section },
        )
        assertEquals(
            listOf("foundation/button", "foundation/icon-button"),
            tree[1].sections.first().pages.map { it.id },
        )
    }

    @Test
    fun `tree omits groups with no pages`() {
        val tree = sidebarTree(entries.filter { it.group != ShowcaseGroup.Components })

        assertEquals(listOf(ShowcaseGroup.Foundations, ShowcaseGroup.ProfileFlow), tree.map { it.group })
    }

    @Test
    fun `a collapsed group lists only its heading, with the full page count`() {
        val rows = sidebarRows(sidebarTree(entries)) { it == ShowcaseGroup.Foundations }

        assertEquals(
            listOf("group:Foundations", "foundation/colors", "group:Components", "group:ProfileFlow"),
            rows.map { it.key },
        )
        assertEquals(3, rows.filterIsInstance<SidebarRow.Group>().single { it.group == ShowcaseGroup.Components }.pageCount)
    }

    @Test
    fun `an expanded group heads each section, and a flat group lists pages directly`() {
        val rows = sidebarRows(sidebarTree(entries)) { true }

        assertEquals(
            listOf(
                "group:Foundations", "foundation/colors",
                "group:Components",
                "section:Actions", "foundation/button", "foundation/icon-button",
                "section:Inputs", "foundation/text-field",
                "group:ProfileFlow",
                "section:ProfileScreens", "flow-profile/profile",
                "section:ProfileSheets", "flow-profile/sheet",
            ),
            rows.map { it.key },
        )
    }

    @Test
    fun `search matches the section name`() {
        val tree = sidebarTree(entries.filter { it.matches("sheets") })

        assertEquals(listOf("flow-profile/sheet"), tree.flatMap { it.sections }.flatMap { it.pages }.map { it.id })
    }

    @Test
    fun `an entry cannot sit in a section of another group`() {
        assertFailsWith<IllegalArgumentException> {
            page("x", ShowcaseGroup.Components, "X", ShowcaseSection.ProfileRows)
        }
    }

    @Test
    fun `registry ids are unique`() {
        val duplicates = ShowcaseRegistry.groupBy { it.id }.filterValues { it.size > 1 }.keys

        assertTrue(duplicates.isEmpty(), "Duplicate ids: $duplicates")
    }

    @Test
    fun `registry titles are unique within a section`() {
        val duplicates = ShowcaseRegistry
            .groupBy { Triple(it.group, it.section, it.title.lowercase()) }
            .filterValues { it.size > 1 }
            .keys

        assertTrue(duplicates.isEmpty(), "Duplicate titles: $duplicates")
    }

    @Test
    fun `every registry group sections all of its pages or none`() {
        val mixed = ShowcaseRegistry.groupBy { it.group }
            .filterValues { pages -> pages.any { it.section == null } && pages.any { it.section != null } }
            .keys

        assertTrue(mixed.isEmpty(), "Groups with sectioned and unsectioned pages: $mixed")
    }

    @Test
    fun `every registry group and section has pages`() {
        val empty = ShowcaseGroup.entries.filter { group -> ShowcaseRegistry.none { it.group == group } } +
            ShowcaseSection.entries.filter { section -> ShowcaseRegistry.none { it.section == section } }

        assertTrue(empty.isEmpty(), "Empty: $empty")
    }

    @Test
    fun `flow page titles do not repeat the flow name`() {
        val flows = mapOf(
            ShowcaseGroup.AuthFlow to "auth ",
            ShowcaseGroup.ProfileFlow to "profile ",
            ShowcaseGroup.AttachmentsFlow to "attachment",
        )
        val redundant = ShowcaseRegistry
            .filter { entry -> flows[entry.group]?.let { entry.title.lowercase().startsWith(it) } == true }
            .map { it.id }

        assertTrue(redundant.isEmpty(), "Titles repeating their flow: $redundant")
    }

    @Test
    fun `registry ids start with a module prefix`() {
        val modules = setOf(
            "foundation", "icons", "charts", "datetime", "keyboard", "crop", "media",
            "flow-auth", "flow-profile", "flow-attachments", "flow-gallery",
        )
        val unknown = ShowcaseRegistry.map { it.id }.filter { it.substringBefore('/') !in modules }

        assertTrue(unknown.isEmpty(), "Ids without a module prefix: $unknown")
    }

    private fun page(
        id: String,
        group: ShowcaseGroup,
        title: String,
        section: ShowcaseSection? = null,
    ) = ShowcaseEntry(
        id = id,
        group = group,
        title = title,
        summary = "",
        section = section,
        content = {},
    )
}
