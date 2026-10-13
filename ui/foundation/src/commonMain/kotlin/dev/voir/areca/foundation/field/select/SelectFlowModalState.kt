package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Search state for a [SelectFlowModal].
 *
 * The modal owns none of the searching. It shows [query] in its search field and shows a loading
 * body while [isSearching] is set; producing the matching options is the caller's job, and they
 * arrive as the modal's `options` like any other list. That split is what lets the same modal
 * filter a list in memory and query a server without knowing which it is doing.
 *
 * The state holds only text and a flag, so one type serves a modal of any value type.
 *
 * @param initialQuery Search text the state starts with.
 */
@Stable
public class SelectFlowModalState(
    initialQuery: String = "",
) {
    /** Current search text. Written by the modal's search field, read by the caller. */
    public var query: String by mutableStateOf(initialQuery)

    /**
     * Whether results for [query] are still on their way. While set, the modal shows its loading
     * body instead of the option list.
     */
    public var isSearching: Boolean by mutableStateOf(false)

    /** Clears [query] without touching [isSearching]. */
    public fun clearQuery() {
        query = ""
    }
}

/**
 * Remembers a [SelectFlowModalState] across recompositions.
 *
 * The state is not saved across process death: a half-typed query is not worth restoring into a
 * modal that will have been dismissed by then.
 *
 * @param initialQuery Query the modal opens with. Only read on first composition.
 * @return State to hand to [SelectFlowModal].
 */
@Composable
public fun rememberSelectFlowModalState(
    initialQuery: String = "",
): SelectFlowModalState {
    return remember {
        SelectFlowModalState(initialQuery = initialQuery)
    }
}
