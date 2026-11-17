package dev.voir.areca.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.Chip
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.collection.ContentListGroup
import dev.voir.areca.foundation.collection.ContentListGroupLayout
import dev.voir.areca.foundation.collection.GroupedPagedContentList
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.navigation.ScrollToTopOnTabReselect
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalTopBar
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.FilterAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

// The "Catalogue with filters" recipe from skills/areca-ui-v1/reference/recipes.md, compiled so the
// recipe cannot drift from the API. Keep the two in step.

/** How the catalogue is ordered. */
internal enum class CatalogueSort { Popular, Newest, Price }

/**
 * The filters in force, owned by the screen's view model.
 *
 * @property query The search text.
 * @property families The families chosen; empty for all.
 * @property sort The order.
 * @property inStockOnly Whether only items in stock are listed.
 */
@Immutable
internal data class CatalogueFilters(
    val query: String = "",
    val families: Set<String> = emptySet(),
    val sort: CatalogueSort = CatalogueSort.Popular,
    val inStockOnly: Boolean = false,
) {
    /** How many filters are in force, for the top bar's count. */
    val count: Int get() = families.size + (if (inStockOnly) 1 else 0)
}

/**
 * One item of the catalogue.
 *
 * @property id Its identity.
 * @property name Its name.
 * @property detail A line under the name.
 */
@Immutable
internal data class CatalogueItem(val id: String, val name: String, val detail: String)

/**
 * A searchable catalogue: the query and the filters in force under the title, the items by family
 * on cards, and a sheet that edits the filters.
 *
 * @param filters The filters in force.
 * @param draft The filters being edited in the sheet, or `null` while the sheet is closed.
 * @param families Every family that can be chosen.
 * @param results The items the filters let through, by family, and where loading them stands.
 * @param onQueryChange Called with the new search text.
 * @param onFiltersChange Called with filters to apply at once — a chip removed.
 * @param onOpenFilters Opens the sheet with the current filters as its draft.
 * @param onDraftChange Called with the draft as it is edited.
 * @param onApplyDraft Applies the draft and closes the sheet.
 * @param onDismissFilters Closes the sheet and drops the draft.
 * @param onLoadNextPage Asks for the next page of results.
 * @param onOpenItem Opens an item.
 */
@Composable
internal fun CatalogueScreen(
    filters: CatalogueFilters,
    draft: CatalogueFilters?,
    families: List<String>,
    results: PagedContentListState<ContentListGroup<String, CatalogueItem>>,
    onQueryChange: (String) -> Unit,
    onFiltersChange: (CatalogueFilters) -> Unit,
    onOpenFilters: () -> Unit,
    onDraftChange: (CatalogueFilters) -> Unit,
    onApplyDraft: () -> Unit,
    onDismissFilters: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpenItem: (CatalogueItem) -> Unit,
) {
    val listState = rememberLazyListState()
    listState.ScrollToTopOnTabReselect()

    BaseLayout(
        topBar = {
            TopBar(
                title = "Catalogue",
                trailing = { FilterAction(onClick = onOpenFilters, badge = filters.count.takeIf { it > 0 }?.toString(), contentDescription = "Filters") },
                bottom = {
                    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                        SearchField(
                            value = filters.query,
                            onValueChange = onQueryChange,
                            modifier = Modifier.horizontalContentInsets(),
                            placeholder = { Text("Search perfumes") },
                        )
                        if (filters.families.isNotEmpty() || filters.inStockOnly) {
                            // The filters in force, each with its own way out.
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                            ) {
                                items(filters.families.toList(), key = { it }) { family ->
                                    Chip(
                                        label = family,
                                        onRemove = { onFiltersChange(filters.copy(families = filters.families - family)) },
                                        removeContentDescription = "Remove $family",
                                    )
                                }
                                if (filters.inStockOnly) {
                                    item(key = "stock") {
                                        Chip(
                                            label = "In stock",
                                            onRemove = { onFiltersChange(filters.copy(inStockOnly = false)) },
                                            removeContentDescription = "Remove In stock",
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
            )
        },
    ) { padding ->
        // Rows on the grey page sit on cards: one inset card per family, its name inside.
        GroupedPagedContentList(
            state = results,
            contentPadding = padding,
            groupLayout = ContentListGroupLayout.Inset,
            listState = listState,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            emptyContent = {
                Fallback(
                    title = "Nothing matches",
                    body = "Try fewer filters or another search.",
                    actionText = "Clear filters",
                    onAction = { onFiltersChange(CatalogueFilters(query = filters.query)) },
                )
            },
            groupHeaderContent = { family -> Text(family) },
        ) { item ->
            ListItem(
                title = { Text(item.name) },
                subtitle = { Text(item.detail) },
                onClick = { onOpenItem(item) },
                onClickLabel = "Open ${item.name}",
            )
        }
    }

    // State-driven: open while there is a draft, and the draft is what the sheet shows.
    FlowModal(
        item = draft,
        onDismissRequest = onDismissFilters,
        header = { FlowModalTopBar(title = "Filters", onClose = onDismissFilters) },
        footer = { FlowModalActionBar(confirmText = "Show results", onConfirm = onApplyDraft) },
    ) { edited ->
        // Flat on the sheet, and scrolling once a long list of families outgrows it.
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            Text("Family", variant = TextVariant.Overline, modifier = Modifier.horizontalContentInsets())
            FlowBadgeSelect(
                options = families.map { BadgeOption(label = { Text(it) }, value = it) },
                onSelect = { option ->
                    val chosen = edited.families
                    onDraftChange(edited.copy(families = if (option.value in chosen) chosen - option.value else chosen + option.value))
                },
                selected = edited.families.toList(),
                selectionMode = BadgeSelectionMode.Multiple,
                style = BadgeStyle.FilledChip,
                size = BadgeSize.Chip,
                modifier = Modifier.horizontalContentInsets(),
            )
            Text("Sort", variant = TextVariant.Overline, modifier = Modifier.horizontalContentInsets())
            SlidingSelector(
                items = CatalogueSort.entries.map { SelectorItem(it) { Text(it.name) } },
                selected = edited.sort,
                onSelectedChange = { onDraftChange(edited.copy(sort = it)) },
                modifier = Modifier.horizontalContentInsets(),
            )
            CheckboxListItem(
                checked = edited.inStockOnly,
                onCheckedChange = { onDraftChange(edited.copy(inStockOnly = it)) },
                title = { Text("In stock only") },
            )
        }
    }
}
