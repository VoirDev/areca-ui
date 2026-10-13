package dev.voir.areca.foundation.navigation

import androidx.compose.runtime.Immutable

/**
 * One destination in a [NavList], or the header of a section.
 *
 * @property id Identity of the destination. Must be unique within one [NavList]; it keys the row,
 *   matches the list's `selectedId`, and keys a section's saved expansion.
 * @property title Label of the row.
 * @property enabled Whether the row accepts input.
 */
@Immutable
public data class NavData(
    public val id: String,
    public val title: String,
    public val enabled: Boolean = true,
)

/** A node of a [NavList]: a top-level destination, or a section of them. */
@Immutable
public sealed class NavNode {
    /**
     * A top-level destination.
     *
     * @property item The destination.
     */
    public data class Item(public val item: NavData) : NavNode()

    /**
     * A header with child items beneath it.
     *
     * @property trigger Header row.
     * @property children Items shown beneath the header.
     * @property expandable Whether clicking the header expands and collapses [children]. A
     *   non-expandable section always shows its children.
     * @property initiallyExpanded Whether an expandable section starts expanded. Ignored when
     *   [expandable] is `false`.
     */
    public data class Section(
        public val trigger: NavData,
        public val children: List<NavData>,
        public val expandable: Boolean = true,
        public val initiallyExpanded: Boolean = false,
    ) : NavNode()
}
