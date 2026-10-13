package dev.voir.areca.foundation.field.select

/**
 * How many options a select control lets the user hold at once.
 */
public enum class SelectSelectionMode {
    /**
     * At most one value. Choosing an option replaces whatever was chosen before, and choosing
     * the current one clears the selection.
     */
    Single,

    /**
     * Any number of values. Each option toggles independently and the list stays open.
     */
    Multiple,
}

/**
 * Applies [option] to [currentValues] under this selection mode.
 *
 * Shared by every select control in the package so that a dropdown and a modal cannot drift
 * apart on what tapping an already-selected option means.
 *
 * @param currentValues Values selected before the interaction.
 * @param option Option the user acted on.
 * @return The values selected afterwards. Under [Single] this is empty when [option] was already
 *   the selection, which is how a caller clears one without a separate control.
 */
internal fun <Value> SelectSelectionMode.applyTo(
    currentValues: Set<Value>,
    option: SelectOption<Value>,
): Set<Value> {
    val value = option.value
    return when (this) {
        SelectSelectionMode.Single -> if (currentValues.contains(value)) {
            emptySet()
        } else {
            setOf(value)
        }

        SelectSelectionMode.Multiple -> if (currentValues.contains(value)) {
            currentValues - value
        } else {
            currentValues + value
        }
    }
}
