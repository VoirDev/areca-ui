package dev.voir.areca.foundation.tabbar

import androidx.compose.runtime.Immutable

/**
 * Exposes the current interaction state while composing a [TabBarItem].
 *
 * @property selected Whether this tab's value is the selected value.
 * @property enabled Whether this tab can be selected.
 */
@Immutable
public data class TabBarItemScope(
    public val selected: Boolean,
    public val enabled: Boolean,
)
