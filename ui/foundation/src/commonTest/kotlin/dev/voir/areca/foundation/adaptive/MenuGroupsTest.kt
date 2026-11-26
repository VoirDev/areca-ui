package dev.voir.areca.foundation.adaptive

import kotlin.test.Test
import kotlin.test.assertEquals

class MenuGroupsTest {
    private fun item(label: String, destructive: Boolean = false) =
        AdaptiveMenuItem(label = label, destructive = destructive, onClick = {})

    private fun labels(groups: List<List<AdaptiveMenuItem>>): List<List<String>> =
        menuGroups(groups).map { group -> group.map(AdaptiveMenuItem::label) }

    @Test
    fun `groups keep their order and the order of their actions`() {
        assertEquals(
            listOf(listOf("Edit", "Duplicate"), listOf("Share")),
            labels(listOf(listOf(item("Edit"), item("Duplicate")), listOf(item("Share")))),
        )
    }

    @Test
    fun `destructive actions leave their groups and come last as one group`() {
        assertEquals(
            listOf(listOf("Edit"), listOf("End"), listOf("Delete", "Discard")),
            labels(
                listOf(
                    listOf(item("Delete", destructive = true), item("Edit")),
                    listOf(item("End"), item("Discard", destructive = true)),
                ),
            ),
        )
    }

    @Test
    fun `a group left empty is dropped so no hairline stands alone`() {
        assertEquals(
            listOf(listOf("Edit"), listOf("Delete")),
            labels(listOf(listOf(item("Edit")), emptyList(), listOf(item("Delete", destructive = true)))),
        )
    }

    @Test
    fun `a menu of one flat group with a destructive action splits into two groups`() {
        assertEquals(
            listOf(listOf("Edit", "End"), listOf("Delete")),
            labels(listOf(listOf(item("Edit"), item("Delete", destructive = true), item("End")))),
        )
    }
}
