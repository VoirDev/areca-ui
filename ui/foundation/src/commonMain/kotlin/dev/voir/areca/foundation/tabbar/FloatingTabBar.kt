package dev.voir.areca.foundation.tabbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Renders a floating, controlled TabBar with one shared animated indicator.
 *
 * Items remain equal-width and use the same generic [TabBarItem] contract as the base [TabBar].
 * The indicator moves as one layer behind the items instead of rendering a selected background in
 * every item.
 *
 * Announced as a group of tabs: a screen reader reports each tab's selected state, and each enabled
 * tab is its own focus stop, reached with the Tab key and selected with Enter or Space.
 *
 * @param items Ordered tab definitions. Item values must be unique.
 * @param selectedValue Currently selected value, or `null` to hide the indicator.
 * @param onValueSelected Called with an enabled item's value when it is selected.
 * @param modifier Modifier applied to the full-width floating TabBar host.
 * @param style Floating container and unselected content treatment.
 * @param size Floating layout and content sizing.
 * @param indicatorStyle Shared indicator geometry and selected content palette.
 * @param windowInsets Safe-area insets applied before the visual outer padding.
 * @param animationSpec Motion used when the indicator changes tab cells. Defaults to the theme's
 *   medium duration on its standard easing, the pace of the [dev.voir.areca.foundation.tabs.Tabs]
 *   indicator.
 */
@Composable
public fun <T : Any> FloatingTabBar(
    items: List<TabBarItem<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: FloatingTabBarStyle = LocalFloatingTabBarStyle.current ?: FloatingTabBarStyle.Default,
    size: FloatingTabBarSize = LocalFloatingTabBarSize.current ?: FloatingTabBarSize.Default,
    indicatorStyle: FloatingTabBarIndicatorStyle = FloatingTabBarIndicatorStyle.underline(),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    ),
    animationSpec: FiniteAnimationSpec<Dp> =
        ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard),
) {
    val selectedIndex = items.indexOfFirst { it.value == selectedValue }.takeIf { it >= 0 }
    val indicatorAlpha = animateFloatAsState(
        targetValue = if (selectedIndex == null) 0f else 1f,
        animationSpec = ArecaTheme.motion.instant.tween(),
        label = "floatingTabBarIndicatorAlpha",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .padding(size.outerPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = size.elevation,
                    shape = style.shape,
                    clip = false,
                )
                // Shaped fill rather than a clip, so an edge item's focus ring is not cropped.
                .background(style.bg, style.shape)
                .padding(size.containerPadding),
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(size.height),
            ) {
                val itemWidth = if (items.isEmpty()) 0.dp else maxWidth / items.size
                val indicatorOffset = animateDpAsState(
                    targetValue = itemWidth * (selectedIndex ?: 0),
                    animationSpec = animationSpec,
                    label = "floatingTabBarIndicatorPosition",
                )

                if (items.isNotEmpty()) {
                    FloatingTabBarIndicator(
                        indicatorStyle = indicatorStyle,
                        itemWidth = itemWidth,
                        offset = indicatorOffset::value,
                        alpha = indicatorAlpha::value,
                    )
                }

                Row(modifier = Modifier.fillMaxSize().selectableGroup()) {
                    items.forEach { item ->
                        FloatingTabBarItem(
                            item = item,
                            selected = item.value == selectedValue,
                            onClick = { onValueSelected(item.value) },
                            style = style,
                            size = size,
                            indicatorStyle = indicatorStyle,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Renders a floating TabBar controlled by remembered [state].
 *
 * @param items Ordered tab definitions. Item values must be unique.
 * @param state State created by [rememberTabBarState] for the same items.
 * @param modifier Modifier applied to the full-width floating TabBar host.
 * @param style Floating container and unselected content treatment.
 * @param size Floating layout and content sizing.
 * @param indicatorStyle Shared indicator geometry and selected content palette.
 * @param windowInsets Safe-area insets applied before the visual outer padding.
 * @param animationSpec Motion used when the indicator changes tab cells. Defaults to the theme's
 *   medium duration on its standard easing, the pace of the [dev.voir.areca.foundation.tabs.Tabs]
 *   indicator.
 */
@Composable
public fun <T : Any> FloatingTabBar(
    items: List<TabBarItem<T>>,
    state: TabBarState<T>,
    modifier: Modifier = Modifier,
    style: FloatingTabBarStyle = LocalFloatingTabBarStyle.current ?: FloatingTabBarStyle.Default,
    size: FloatingTabBarSize = LocalFloatingTabBarSize.current ?: FloatingTabBarSize.Default,
    indicatorStyle: FloatingTabBarIndicatorStyle = FloatingTabBarIndicatorStyle.underline(),
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    ),
    animationSpec: FiniteAnimationSpec<Dp> =
        ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard),
) {
    FloatingTabBar(
        items = items,
        selectedValue = state.selectedValue,
        onValueSelected = state::select,
        modifier = modifier,
        style = style,
        size = size,
        indicatorStyle = indicatorStyle,
        windowInsets = windowInsets,
        animationSpec = animationSpec,
    )
}

/**
 * The one indicator shared by every cell of a [FloatingTabBar], drawn behind the items.
 *
 * Offset from the bar's start edge, so it follows the items' reading order in a right-to-left
 * layout.
 *
 * @param indicatorStyle Geometry and color of the indicator.
 * @param itemWidth Width of one equal-width cell.
 * @param offset Reads the indicator's distance from the start edge. Called only in placement, so
 *   a travelling indicator does not recompose the bar.
 * @param alpha Reads the indicator's opacity. Called only while drawing.
 */
@Composable
private fun FloatingTabBarIndicator(
    indicatorStyle: FloatingTabBarIndicatorStyle,
    itemWidth: Dp,
    offset: () -> Dp,
    alpha: () -> Float,
) {
    val cellModifier = Modifier
        .offset { IntOffset(x = offset().roundToPx(), y = 0) }
        .width(itemWidth)
        .fillMaxHeight()
        .graphicsLayer { this.alpha = alpha() }

    when (indicatorStyle) {
        is FloatingTabBarIndicatorStyle.Underline -> {
            Box(
                modifier = cellModifier.padding(bottom = indicatorStyle.bottomPadding),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Box(
                    modifier = Modifier
                        .width(indicatorStyle.width)
                        .height(indicatorStyle.height)
                        .background(indicatorStyle.color, indicatorStyle.shape),
                )
            }
        }

        is FloatingTabBarIndicatorStyle.Fill -> {
            Box(
                modifier = cellModifier.padding(indicatorStyle.padding),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(indicatorStyle.color, indicatorStyle.shape),
                )
            }
        }
    }
}

/**
 * One equal-width cell of a [FloatingTabBar].
 *
 * @param item Tab definition to render.
 * @param selected Whether this item's value is the selected value.
 * @param onClick Called when the user selects the item.
 * @param style Container, content colors and feedback of the bar.
 * @param size Metrics of the bar, also re-provided to the item's content.
 * @param indicatorStyle Indicator whose selected content color the selected item takes.
 */
@Composable
private fun <T : Any> RowScope.FloatingTabBarItem(
    item: TabBarItem<T>,
    selected: Boolean,
    onClick: () -> Unit,
    style: FloatingTabBarStyle,
    size: FloatingTabBarSize,
    indicatorStyle: FloatingTabBarIndicatorStyle,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor by animateColorAsState(
        targetValue = when {
            !item.enabled -> style.contentDisabled
            selected -> indicatorStyle.contentSelected
            else -> style.content
        },
        animationSpec = ArecaTheme.motion.fast.tween(),
        label = "floatingTabBarItemContent",
    )

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalIconSize provides size.iconSize,
        LocalTextStyle provides size.textStyle,
        LocalFloatingTabBarSize provides size,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
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
