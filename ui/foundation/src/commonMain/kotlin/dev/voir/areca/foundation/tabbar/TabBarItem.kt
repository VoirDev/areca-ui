package dev.voir.areca.foundation.tabbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * Defines one selectable value and its caller-owned content in a [TabBar].
 *
 * Values must be unique within the containing TabBar so selection identifies exactly one tab.
 *
 * @property value Stable value reported when this tab is selected.
 * @property enabled Whether the tab accepts selection.
 * @property content Content rendered for the tab with its current interaction state.
 */
@Immutable
public data class TabBarItem<T : Any>(
    public val value: T,
    public val enabled: Boolean = true,
    public val content: @Composable TabBarItemScope.() -> Unit,
)
