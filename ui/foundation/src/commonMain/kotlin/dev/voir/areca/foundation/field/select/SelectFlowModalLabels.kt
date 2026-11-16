package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Immutable

/**
 * Copy a [SelectFlowModal] shows in its stock search field, faces and rows.
 *
 * The library ships no localization, so the words are parameters: pass your own translations, or
 * [English] for an English-only product. The wordings that name the query are functions of it, so a
 * language can put the query wherever its grammar wants it.
 *
 * @property search Hint in the search field while the query is empty.
 * @property loading Body shown during a first load, before anything was typed.
 * @property searching Body shown while results for the query are on their way.
 * @property empty Body shown when there is nothing to offer and nothing was typed.
 * @property noResults Body shown when nothing matched the query.
 * @property createOption Caption of the row that adds the query as an option of the user's own.
 * @property indexOther Letter header of the labels in an indexed section that do not start with a
 *   letter: digits, symbols, emoji.
 */
@Immutable
public data class SelectFlowModalLabels(
    public val search: String,
    public val loading: String,
    public val searching: (query: String) -> String,
    public val empty: String,
    public val noResults: (query: String) -> String,
    public val createOption: (query: String) -> String,
    public val indexOther: String,
) {
    /** Ready-made label sets. */
    public companion object {
        /** English wording. */
        public val English: SelectFlowModalLabels = SelectFlowModalLabels(
            search = "Search",
            loading = "Loading...",
            searching = { query -> "Searching \"$query\"..." },
            empty = "No items",
            noResults = { query -> "No results for \"$query\"" },
            createOption = { query -> "Add “$query” as your own" },
            indexOther = "#",
        )
    }
}
