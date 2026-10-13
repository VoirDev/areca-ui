package dev.voir.areca.foundation.tabbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Holds locally controlled TabBar selection.
 *
 * Selection is always either an enabled value or `null` when no values are enabled. Use
 * [rememberTabBarState] to create this state from a list of tab items.
 *
 * Observable rather than immutable: [selectedValue] changes as the user selects tabs, and readers
 * recompose when it does.
 *
 * @param initialSelectedValue Preferred initial value, or `null` to select the first enabled value.
 * @param initialEnabledValues Enabled values in display order, used to validate and recover
 *   selection.
 */
@Stable
public class TabBarState<T : Any> internal constructor(
    initialSelectedValue: T?,
    initialEnabledValues: List<T>,
) {
    /** Values that may currently be selected. */
    private var enabledValues = initialEnabledValues.toSet()

    /** Currently selected enabled value, or `null` when every item is disabled or absent. */
    public var selectedValue: T? by mutableStateOf(
        initialSelectedValue.resolveSelection(initialEnabledValues),
    )
        private set

    /**
     * Selects [value] when it belongs to a currently enabled item.
     *
     * Disabled and unknown values are ignored.
     *
     * @param value Tab value to select.
     */
    public fun select(value: T) {
        if (value in enabledValues) {
            selectedValue = value
        }
    }

    /**
     * Revalidates selection after the enabled values or their display order changes.
     *
     * @param values Enabled values in their current display order.
     */
    internal fun updateEnabledValues(values: List<T>) {
        enabledValues = values.toSet()
        selectedValue = selectedValue.resolveSelection(values)
    }
}

/**
 * Remembers local TabBar selection and keeps it valid as [items] change.
 *
 * The requested initial value is selected only when its item is enabled. Otherwise, the first
 * enabled value is selected. Removing or disabling the selected value applies the same fallback,
 * and the selection becomes `null` when no values are enabled.
 *
 * The selection is saved, so it survives configuration change and process death, whenever the
 * platform can store the selected value itself: a `String`, a number, an enum constant. Any other
 * value is kept only for as long as the composition lives. A restored value that is no longer
 * enabled falls back like any other.
 *
 * @param items Ordered tab definitions used to validate selection.
 * @param initialSelectedValue Preferred value on the first composition.
 * @return Stable state that controls local TabBar selection.
 */
@Composable
public fun <T : Any> rememberTabBarState(
    items: List<TabBarItem<T>>,
    initialSelectedValue: T? = null,
): TabBarState<T> {
    val enabledValues = items.filter { it.enabled }.map { it.value }
    val state = rememberSaveable(saver = tabBarStateSaver(enabledValues)) {
        TabBarState(
            initialSelectedValue = initialSelectedValue,
            initialEnabledValues = enabledValues,
        )
    }

    LaunchedEffect(enabledValues) {
        state.updateEnabledValues(enabledValues)
    }

    return state
}

/**
 * Saves a [TabBarState] as its selected value, when the platform can store that value.
 *
 * A value the registry cannot store, and an empty selection, are not saved at all, because handing
 * an unstorable value over makes `rememberSaveable` throw on Android.
 *
 * @param enabledValues Enabled values in display order, which a restored selection is checked
 *   against.
 * @return Saver that stores storable selections and skips the rest.
 */
internal fun <T : Any> tabBarStateSaver(enabledValues: List<T>): Saver<TabBarState<T>, T> = Saver(
    save = { state -> state.selectedValue?.takeIf { canBeSaved(it) } },
    restore = { saved ->
        TabBarState(initialSelectedValue = saved, initialEnabledValues = enabledValues)
    },
)

/**
 * Preserves a valid selection or falls back to the first enabled value in display order.
 *
 * @param enabledValues Enabled values in display order.
 * @return This value when it is enabled, otherwise the first enabled value, or `null` when none is.
 */
private fun <T : Any> T?.resolveSelection(enabledValues: List<T>): T? {
    return takeIf { selectedValue -> selectedValue in enabledValues }
        ?: enabledValues.firstOrNull()
}
