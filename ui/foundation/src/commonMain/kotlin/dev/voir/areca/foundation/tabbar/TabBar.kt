package dev.voir.areca.foundation.tabbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Renders a controlled, equal-width row of selectable tab content, docked to the bottom of a
 * screen.
 *
 * Only enabled items invoke [onValueSelected]. Selection ownership remains with the caller.
 *
 * The bar's fill runs under [windowInsets] while its items clear them, so a bar docked over the
 * system navigation area reaches the screen edge without its tabs sitting under the gesture handle.
 *
 * Announced as a group of tabs: a screen reader reports each tab's selected state, and each enabled
 * tab is its own focus stop, reached with the Tab key and selected with Enter or Space.
 *
 * @param items Ordered tab definitions. Item values must be unique.
 * @param selectedValue Currently selected value, or `null` when nothing is selected.
 * @param onValueSelected Called with an enabled item's value when the item is selected.
 * @param modifier Modifier applied to the full-width TabBar container.
 * @param style Colors, item shape and feedback used by the container and item states.
 * @param size Layout and content sizing used by every item.
 * @param windowInsets Platform insets the items are padded by, inside the bar's fill. The
 *   horizontal and bottom safe-drawing insets by default; pass `WindowInsets(0, 0, 0, 0)` when the
 *   surrounding layout already applies them.
 */
@Composable
public fun <T : Any> TabBar(
    items: List<TabBarItem<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: TabBarStyle = LocalTabBarStyle.current ?: TabBarStyle.Default,
    size: TabBarSize = LocalTabBarSize.current ?: TabBarSize.Default,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    ),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(style.bg)
            .windowInsetsPadding(windowInsets)
            .selectableGroup(),
    ) {
        items.forEach { item ->
            TabBarItem(
                item = item,
                selected = item.value == selectedValue,
                onClick = { onValueSelected(item.value) },
                style = style,
                size = size,
            )
        }
    }
}

/**
 * Renders an equal-width row of tabs controlled by remembered [state].
 *
 * @param items Ordered tab definitions. Item values must be unique.
 * @param state State created by [rememberTabBarState] for the same items.
 * @param modifier Modifier applied to the full-width TabBar container.
 * @param style Colors, item shape and feedback used by the container and item states.
 * @param size Layout and content sizing used by every item.
 * @param windowInsets Platform insets the items are padded by, inside the bar's fill.
 */
@Composable
public fun <T : Any> TabBar(
    items: List<TabBarItem<T>>,
    state: TabBarState<T>,
    modifier: Modifier = Modifier,
    style: TabBarStyle = LocalTabBarStyle.current ?: TabBarStyle.Default,
    size: TabBarSize = LocalTabBarSize.current ?: TabBarSize.Default,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    ),
) {
    TabBar(
        items = items,
        selectedValue = state.selectedValue,
        onValueSelected = state::select,
        modifier = modifier,
        style = style,
        size = size,
        windowInsets = windowInsets,
    )
}

/**
 * One equal-width cell of a [TabBar].
 *
 * @param item Tab definition to render.
 * @param selected Whether this item's value is the selected value.
 * @param onClick Called when the user selects the item.
 * @param style Colors, shape and feedback of the bar.
 * @param size Metrics of the bar.
 */
@Composable
private fun <T : Any> RowScope.TabBarItem(
    item: TabBarItem<T>,
    selected: Boolean,
    onClick: () -> Unit,
    style: TabBarStyle,
    size: TabBarSize,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val background by animateColorAsState(
        targetValue = when {
            !item.enabled -> style.itemBgDisabled
            selected -> style.itemBgSelected
            else -> style.itemBg
        },
        animationSpec = ArecaTheme.motion.fast.tween(),
        label = "tabBarItemBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            !item.enabled -> style.contentDisabled
            selected -> style.contentSelected
            else -> style.content
        },
        animationSpec = ArecaTheme.motion.fast.tween(),
        label = "tabBarItemContent",
    )

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalIconSize provides size.iconSize,
        LocalTextStyle provides size.textStyle,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = size.itemMinHeight)
                .background(background, style.itemShape)
                .selectable(
                    selected = selected,
                    enabled = item.enabled,
                    interactionSource = interactionSource,
                    indication = style.indication,
                    role = Role.Tab,
                    onClick = onClick,
                )
                .clickCursor(item.enabled)
                .padding(size.itemPadding),
            contentAlignment = Alignment.Center,
        ) {
            item.content(
                TabBarItemScope(
                    selected = selected,
                    enabled = item.enabled,
                )
            )
        }
    }
}
