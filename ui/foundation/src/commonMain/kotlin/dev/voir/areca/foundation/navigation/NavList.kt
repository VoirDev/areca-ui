package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.layout.Expandable
import dev.voir.areca.foundation.layout.VerticalScrollbarOverlay
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Spacing and indentation of a [NavList].
 *
 * The rows themselves take [NavItemSize] from [LocalNavItemSize]; this only says how they are laid
 * out relative to each other.
 *
 * @property menuArrangement Vertical spacing between top-level nodes (items and section headers).
 * @property subMenuArrangement Vertical spacing between children inside an expanded section.
 * @property sectionGap Extra gap between a section header and its first child.
 * @property indent Start indentation of a section's children, expressing hierarchy.
 */
@Immutable
public data class NavListSize(
    public val menuArrangement: Dp,
    public val subMenuArrangement: Dp,
    public val sectionGap: Dp,
    public val indent: Dp,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default. */
        public val Default: NavListSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme's spacing scale.
         *
         * @param menuArrangement Vertical spacing between top-level nodes.
         * @param subMenuArrangement Vertical spacing between children in an expanded section.
         * @param sectionGap Extra gap between a section header and its children.
         * @param indent Start indentation of a section's children.
         * @return Size for a navigation list.
         */
        @Composable
        public fun default(
            menuArrangement: Dp = ArecaTheme.spacing.md,
            subMenuArrangement: Dp = ArecaTheme.spacing.xs,
            sectionGap: Dp = ArecaTheme.spacing.xs,
            indent: Dp = ArecaTheme.spacing.xl,
        ): NavListSize = NavListSize(
            menuArrangement = menuArrangement,
            subMenuArrangement = subMenuArrangement,
            sectionGap = sectionGap,
            indent = indent,
        )
    }
}

/** Default [NavListSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNavListSize: ProvidableCompositionLocal<NavListSize?> = compositionLocalOf { null }

/**
 * Renders a vertical navigation list with optional expandable sections.
 *
 * The list supports top-level items, section headers that expand and collapse to reveal their
 * children, and highlighting of the current destination via [selectedId].
 *
 * Expansion is held per section id, so it survives a new [nodes] list that keeps the same sections,
 * and is saved with the screen, so it survives configuration changes and process death. A section
 * the user has not toggled follows [NavNode.Section.initiallyExpanded]; one with
 * [NavNode.Section.expandable] set to `false` always shows its children. An expandable header
 * reports its expanded or collapsed state, with the matching action, to assistive technology.
 *
 * The rows take [NavItemStyle] and [NavItemSize] from [LocalNavItemStyle] and [LocalNavItemSize].
 *
 * @param nodes The navigation model to render (items and sections).
 * @param selectedId Id of the current destination, shown and announced as selected, or `null`.
 * @param onItemClick Called when a leaf item is clicked, at the top level or inside a section.
 * @param modifier Modifier applied to the list.
 * @param size Spacing and indentation. Defaults to the theme's.
 */
@Composable
public fun NavList(
    nodes: List<NavNode>,
    selectedId: String?,
    onItemClick: (NavData) -> Unit,
    modifier: Modifier = Modifier,
    size: NavListSize = LocalNavListSize.current ?: NavListSize.Default,
) {
    var toggledSections by rememberSaveable(stateSaver = NavExpansionSaver) {
        mutableStateOf(emptyMap<String, Boolean>())
    }

    val listState = rememberLazyListState()
    Box(modifier = modifier.fillMaxWidth(), propagateMinConstraints = true) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(size.menuArrangement),
        ) {
            items(
                items = nodes,
                key = { node ->
                    when (node) {
                        is NavNode.Item -> "item:${node.item.id}"
                        is NavNode.Section -> "section:${node.trigger.id}"
                    }
                },
            ) { node ->
                when (node) {
                    is NavNode.Item -> NavItem(
                        selected = node.item.id == selectedId,
                        enabled = node.item.enabled,
                        onClick = { onItemClick(node.item) },
                    ) {
                        Text(node.item.title)
                    }

                    is NavNode.Section -> {
                        val sectionId = node.trigger.id
                        val expanded = node.isShownExpanded(toggledState = toggledSections[sectionId])
                        val toggle = { toggledSections = toggledSections + (sectionId to !expanded) }

                        NavItem(
                            modifier = if (node.expandable) {
                                Modifier.semantics {
                                    if (expanded) {
                                        collapse { toggle(); true }
                                    } else {
                                        expand { toggle(); true }
                                    }
                                }
                            } else {
                                Modifier
                            },
                            selected = sectionId == selectedId,
                            enabled = node.trigger.enabled,
                            onClick = if (node.expandable) toggle else null,
                            trailing = if (node.expandable) {
                                {
                                    Chevron(
                                        direction = if (expanded) {
                                            ChevronDirection.Down
                                        } else {
                                            ChevronDirection.Right
                                        },
                                    )
                                }
                            } else {
                                null
                            },
                        ) {
                            Text(node.trigger.title)
                        }

                        Expandable(expanded = expanded) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(size.subMenuArrangement),
                            ) {
                                Spacer(modifier = Modifier.height(size.sectionGap))
                                node.children.forEach { child ->
                                    NavItem(
                                        selected = child.id == selectedId,
                                        indent = size.indent,
                                        enabled = child.enabled,
                                        onClick = { onItemClick(child) },
                                    ) {
                                        Text(child.title)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        VerticalScrollbarOverlay(listState)
    }
}

/**
 * Whether this section's children are shown.
 *
 * A non-expandable section has no header click to reveal its children, so it is always expanded
 * and [NavNode.Section.initiallyExpanded] is ignored.
 *
 * @param toggledState Expansion state held for the section, or `null` when none is held yet, in
 *   which case [NavNode.Section.initiallyExpanded] applies.
 * @return `true` when the section's children are shown.
 */
internal fun NavNode.Section.isShownExpanded(toggledState: Boolean?): Boolean =
    !expandable || (toggledState ?: initiallyExpanded)

/**
 * Flattens the sections a user has toggled into a list the platform can save.
 *
 * @param toggled Expansion the user chose, by section id.
 * @return Alternating section ids and expansion flags.
 */
internal fun navExpansionToSaveable(toggled: Map<String, Boolean>): List<Any> {
    val flat = ArrayList<Any>(toggled.size * 2)
    toggled.forEach { (id, expanded) ->
        flat += id
        flat += expanded
    }
    return flat
}

/**
 * Rebuilds the toggled sections from [navExpansionToSaveable]'s output.
 *
 * @param saved Alternating section ids and expansion flags. Pairs of any other shape are skipped.
 * @return Expansion the user chose, by section id.
 */
internal fun navExpansionFromSaveable(saved: List<Any>): Map<String, Boolean> =
    saved.chunked(2).mapNotNull { pair ->
        val id = pair.getOrNull(0) as? String
        val expanded = pair.getOrNull(1) as? Boolean
        if (id != null && expanded != null) id to expanded else null
    }.toMap()

/** Saves the sections a user has toggled across configuration changes and process death. */
private val NavExpansionSaver: Saver<Map<String, Boolean>, Any> = Saver(
    save = { toggled -> navExpansionToSaveable(toggled) },
    restore = { saved -> (saved as? List<*>)?.filterNotNull()?.let(::navExpansionFromSaveable) },
)
