package dev.voir.areca.playground.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.playground.registry.ShowcaseEntry
import dev.voir.areca.playground.registry.ShowcaseGroup
import dev.voir.areca.playground.showcase.chrome.ChromeInput
import dev.voir.areca.playground.showcase.chrome.ChromeNavGroupToggle
import dev.voir.areca.playground.showcase.chrome.ChromeNavItem
import dev.voir.areca.playground.showcase.chrome.ChromeNavSectionHeader
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.flow.first

/** Rows kept above the shown page when the list scrolls to reveal it. */
private const val RevealContextRows = 2

/** Saves the set of expanded groups by enum name, which every platform's saved state can hold. */
private val ExpandedGroupsSaver = Saver<Set<ShowcaseGroup>, List<String>>(
    save = { groups -> groups.map(ShowcaseGroup::name) },
    restore = { names ->
        names.mapNotNull { name -> ShowcaseGroup.entries.find { it.name == name } }.toSet()
    },
)

/**
 * The playground's page list: a tree of collapsible groups, each divided into sections of pages.
 *
 * Which groups are expanded is the reader's own and survives navigation, except that the group
 * holding the page shown always opens, and the list scrolls to the page when it is out of view. A
 * search lists every matching page with its groups expanded, and collapsing one there does not
 * touch the browsing state.
 *
 * Built entirely from chrome primitives rather than the design system's own `NavList`: navigation
 * has to keep working while the component it navigates to is broken, and the nav list has a showcase
 * of its own. See `SHOWCASES.md` §5.
 *
 * @param entries Pages to list, in any order; the tree sorts them.
 * @param selectedId Id of the page currently shown, from the navigation back stack.
 * @param onSelect Invoked with the id of a chosen page.
 * @param modifier Modifier applied to the sidebar.
 */
@Composable
fun ShowcaseSidebar(
    entries: List<ShowcaseEntry>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val searching = query.isNotBlank()

    val selectedGroup = remember(entries, selectedId) {
        entries.firstOrNull { it.id == selectedId }?.group
    }
    var expanded by rememberSaveable(stateSaver = ExpandedGroupsSaver) {
        mutableStateOf(setOfNotNull(selectedGroup))
    }
    // Starts fully expanded for every new search, so a match is never hidden in a collapsed group.
    var collapsedInSearch by remember(query) { mutableStateOf(emptySet<ShowcaseGroup>()) }

    val tree = remember(entries, query) { sidebarTree(entries.filter { it.matches(query) }) }
    val rows = if (searching) {
        sidebarRows(tree) { it !in collapsedInSearch }
    } else {
        sidebarRows(tree) { it in expanded }
    }

    val listState = rememberLazyListState()
    val currentRows by rememberUpdatedState(rows)
    LaunchedEffect(selectedId) {
        if (selectedGroup != null) expanded = expanded + selectedGroup
        // Wait for a layout of the current rows — on first open there has been none yet, and an
        // expanded group adds rows — so visibility is judged on what is actually on screen.
        snapshotFlow { listState.layoutInfo }
            .first { it.totalItemsCount == currentRows.size && it.visibleItemsInfo.isNotEmpty() }
        listState.revealPage(currentRows, selectedId)
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        ChromeInput(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search pages",
            modifier = Modifier.fillMaxWidth(),
        )

        if (rows.isEmpty()) {
            NoMatches(query = query)
        } else {
            SidebarList(
                rows = rows,
                selectedId = selectedId,
                onSelectPage = onSelect,
                onToggleGroup = { group ->
                    if (searching) {
                        collapsedInSearch = collapsedInSearch.toggle(group)
                    } else {
                        expanded = expanded.toggle(group)
                    }
                },
                state = listState,
            )
        }
    }
}

/**
 * The flattened tree: group headings, section headings and pages.
 *
 * @param rows What to list, in order.
 * @param selectedId Id of the page currently shown, highlighted where it is listed.
 * @param onSelectPage Invoked with the id of a chosen page.
 * @param onToggleGroup Invoked with a group whose heading was chosen.
 * @param state Scroll state of the list.
 */
@Composable
private fun SidebarList(
    rows: List<SidebarRow>,
    selectedId: String?,
    onSelectPage: (String) -> Unit,
    onToggleGroup: (ShowcaseGroup) -> Unit,
    state: LazyListState,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state,
    ) {
        items(
            count = rows.size,
            key = { index -> rows[index].key },
        ) { index ->
            when (val row = rows[index]) {
                is SidebarRow.Group -> ChromeNavGroupToggle(
                    label = row.group.label,
                    count = row.pageCount,
                    expanded = row.expanded,
                    onToggle = { onToggleGroup(row.group) },
                )

                is SidebarRow.Section -> ChromeNavSectionHeader(label = row.section.label)

                is SidebarRow.Page -> ChromeNavItem(
                    label = row.entry.title,
                    selected = row.entry.id == selectedId,
                    onClick = { onSelectPage(row.entry.id) },
                )
            }
        }
    }
}

/**
 * Stands in for the list when a search matches no page.
 *
 * @param query The search that matched nothing, quoted back to the reader.
 */
@Composable
private fun NoMatches(query: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ArecaTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        ChromeText(
            text = "Nothing matches \"$query\"",
            role = ChromeTextRole.Detail,
            tone = ChromeTone.Muted,
        )
    }
}

/**
 * Scrolls the page [pageId] into view when its row is not fully visible, keeping a couple of rows
 * of context above it. Leaves the list alone when the row is already on screen, so choosing a page
 * never moves the list under the pointer.
 *
 * @param rows Rows the list currently draws.
 * @param pageId Id of the page to reveal; nothing happens when it is `null` or not listed.
 */
private suspend fun LazyListState.revealPage(rows: List<SidebarRow>, pageId: String?) {
    val index = rows.indexOfFirst { it is SidebarRow.Page && it.entry.id == pageId }
    if (index < 0) return

    val viewport = layoutInfo.viewportStartOffset..layoutInfo.viewportEndOffset
    val fullyVisible = layoutInfo.visibleItemsInfo.any { item ->
        item.index == index && item.offset in viewport && item.offset + item.size in viewport
    }
    if (!fullyVisible) scrollToItem((index - RevealContextRows).coerceAtLeast(0))
}

/**
 * This set with [group] removed when present and added when not.
 *
 * @param group Group to flip.
 * @return The updated set.
 */
private fun Set<ShowcaseGroup>.toggle(group: ShowcaseGroup): Set<ShowcaseGroup> =
    if (group in this) this - group else this + group
