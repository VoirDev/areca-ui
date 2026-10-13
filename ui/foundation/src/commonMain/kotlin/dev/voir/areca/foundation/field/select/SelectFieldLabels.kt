package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * What assistive technology reads for the dropdown state of a [SelectField] or [SelectSegment].
 *
 * The library ships no localization, so the words are parameters: pass your own translations, or
 * [English] for an English-only product.
 *
 * @property expanded Read while the dropdown is open.
 * @property collapsed Read while the dropdown is closed.
 */
@Immutable
public data class SelectFieldLabels(
    public val expanded: String,
    public val collapsed: String,
) {
    /** Ready-made label sets. */
    public companion object {
        /** English wording. */
        public val English: SelectFieldLabels = SelectFieldLabels(
            expanded = "Expanded",
            collapsed = "Collapsed",
        )
    }
}

/**
 * Marks a select trigger as a dropdown list and reports whether its dropdown is open.
 *
 * Announced as a dropdown with its open state, and offers the platform's expand and collapse
 * actions so a screen reader can operate it without the generic click. A trigger that cannot open
 * right now keeps the role and drops the state and the actions, since neither would be true.
 *
 * @param expanded Whether the dropdown is open.
 * @param canOpen Whether the trigger can open the dropdown right now.
 * @param labels Words read for the open and closed states.
 * @param onExpand Opens the dropdown.
 * @param onCollapse Closes the dropdown.
 * @return The modifier with the dropdown semantics attached.
 */
internal fun Modifier.selectTriggerSemantics(
    expanded: Boolean,
    canOpen: Boolean,
    labels: SelectFieldLabels,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
): Modifier = semantics {
    role = Role.DropdownList
    if (canOpen) {
        stateDescription = if (expanded) labels.expanded else labels.collapsed
        if (expanded) {
            collapse {
                onCollapse()
                true
            }
        } else {
            expand {
                onExpand()
                true
            }
        }
    }
}
