package dev.voir.areca.playground.shell

import dev.voir.areca.playground.registry.ShowcaseEntry
import dev.voir.areca.playground.registry.ShowcaseGroup
import dev.voir.areca.playground.registry.ShowcaseSection

/**
 * A group as the sidebar lays it out.
 *
 * @property group The group.
 * @property sections Its pages under their headings, in [ShowcaseSection] order, never empty.
 */
internal data class SidebarGroup(
    val group: ShowcaseGroup,
    val sections: List<SidebarSection>,
) {
    /** How many pages the group holds across all its sections. */
    val pageCount: Int get() = sections.sumOf { it.pages.size }
}

/**
 * A run of pages under one heading inside a [SidebarGroup].
 *
 * @property section The heading, or `null` for a group listed flat.
 * @property pages Pages sorted by title, never empty.
 */
internal data class SidebarSection(
    val section: ShowcaseSection?,
    val pages: List<ShowcaseEntry>,
)

/** One row of the sidebar's flattened list. */
internal sealed interface SidebarRow {
    /** Stable key for lazy-list identity, unique across the whole list. */
    val key: String

    /**
     * A group heading that collapses and expands the group.
     *
     * @property group The group.
     * @property pageCount How many pages the group holds; never zero, because empty groups are not
     *   listed.
     * @property expanded Whether the group's pages are listed below it.
     */
    data class Group(
        val group: ShowcaseGroup,
        val pageCount: Int,
        val expanded: Boolean,
    ) : SidebarRow {
        override val key: String get() = "group:${group.name}"
    }

    /**
     * A section heading inside an expanded group.
     *
     * @property section The section.
     */
    data class Section(val section: ShowcaseSection) : SidebarRow {
        override val key: String get() = "section:${section.name}"
    }

    /**
     * A page that opens when chosen.
     *
     * @property entry The page.
     */
    data class Page(val entry: ShowcaseEntry) : SidebarRow {
        override val key: String get() = entry.id
    }
}

/**
 * Lays [entries] out as the sidebar shows them: groups in [ShowcaseGroup] order, sections in
 * [ShowcaseSection] order, and pages alphabetically by title within each.
 *
 * Sorting here rather than in the registry means no one keeps a hand order that drifts.
 *
 * @param entries Pages to lay out, in any order; pass only the matching ones to show a search.
 * @return Non-empty groups; empty when [entries] is.
 */
internal fun sidebarTree(entries: List<ShowcaseEntry>): List<SidebarGroup> {
    val sectionOrder = listOf<ShowcaseSection?>(null) + ShowcaseSection.entries

    return ShowcaseGroup.entries.mapNotNull { group ->
        val inGroup = entries.filter { it.group == group }
        val sections = sectionOrder.mapNotNull { section ->
            inGroup.filter { it.section == section }
                .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, ShowcaseEntry::title))
                .takeIf(List<ShowcaseEntry>::isNotEmpty)
                ?.let { SidebarSection(section = section, pages = it) }
        }

        sections.takeIf(List<SidebarSection>::isNotEmpty)
            ?.let { SidebarGroup(group = group, sections = it) }
    }
}

/**
 * Flattens [tree] into the rows a lazy list draws: each group's heading, and — for an expanded
 * group — its section headings and pages.
 *
 * @param tree Groups from [sidebarTree].
 * @param isExpanded Whether a group's pages are listed.
 * @return Rows in display order.
 */
internal fun sidebarRows(
    tree: List<SidebarGroup>,
    isExpanded: (ShowcaseGroup) -> Boolean,
): List<SidebarRow> = tree.flatMap { group ->
    val expanded = isExpanded(group.group)
    val heading = SidebarRow.Group(group.group, pageCount = group.pageCount, expanded = expanded)
    if (!expanded) return@flatMap listOf(heading)

    listOf(heading) + group.sections.flatMap { section ->
        listOfNotNull(section.section?.let(SidebarRow::Section)) +
            section.pages.map(SidebarRow::Page)
    }
}
