package dev.voir.areca.foundation.tabbar

import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VectorizedFiniteAnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.selection.SelectorCore
import dev.voir.areca.foundation.selection.SelectorEdge
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SelectorOrientation
import dev.voir.areca.foundation.selection.SelectorRole
import dev.voir.areca.foundation.selection.SelectorThumbKind
import dev.voir.areca.foundation.selection.SlidingSelectorSize
import dev.voir.areca.foundation.selection.SlidingSelectorStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.slide

/**
 * Renders a floating, controlled TabBar with one shared animated indicator.
 *
 * Items remain equal-width and use the same generic [TabBarItem] contract as the base [TabBar].
 * Built on [SlidingSelector][dev.voir.areca.foundation.selection.SlidingSelector]: the indicator is
 * its thumb, one layer behind the items that slides between them, grows in when a value is selected
 * after none and shrinks away when the selection is cleared.
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
 * @param animationSpec Motion used when the indicator changes tab cells, grows in or shrinks away.
 *   Defaults to the theme's [slide] spring, the motion of every
 *   [SlidingSelector][dev.voir.areca.foundation.selection.SlidingSelector].
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
    animationSpec: FiniteAnimationSpec<Dp> = ArecaTheme.motion.slide(),
) {
    val selectedIndex = items.indexOfFirst { it.value == selectedValue }
    val selectorStyle = SlidingSelectorStyle.default(
        content = style.content,
        contentSelected = indicatorStyle.contentSelected,
        contentDisabled = style.contentDisabled,
        track = Color.Transparent,
        trackPadding = 0.dp,
        thumb = indicatorStyle.color,
        thumbShape = indicatorStyle.shape,
        thumbElevation = 0.dp,
        itemShape = style.itemShape,
        indication = style.indication,
        motion = DpSpecInPixels(animationSpec),
    ).let { base ->
        when (indicatorStyle) {
            is FloatingTabBarIndicatorStyle.Underline -> base.copy(
                thumbKind = SelectorThumbKind.Underline,
                thumbThickness = indicatorStyle.height,
                thumbLength = indicatorStyle.width,
                thumbPadding = PaddingValues(bottom = indicatorStyle.bottomPadding),
                thumbEdge = SelectorEdge.End,
            )

            is FloatingTabBarIndicatorStyle.Fill -> base.copy(thumbPadding = indicatorStyle.padding)
        }
    }

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
            SelectorCore(
                count = items.size,
                selectedIndex = selectedIndex,
                isEnabled = { items[it].enabled },
                // Selecting the selected tab again is reported, so a screen can scroll to its top.
                onTap = { index -> onValueSelected(items[index].value) },
                role = SelectorRole.Tab,
                style = selectorStyle,
                size = SlidingSelectorSize(
                    gap = 0.dp,
                    itemPadding = size.itemPadding,
                    itemMinWidth = 0.dp,
                    itemMinHeight = 0.dp,
                    iconSize = size.iconSize,
                    textStyle = size.textStyle,
                ),
                layout = SelectorLayout.Fill,
                orientation = SelectorOrientation.Horizontal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(size.height),
                itemModifier = Modifier.fillMaxHeight(),
            ) { index, selected ->
                val item = items[index]
                CompositionLocalProvider(LocalFloatingTabBarSize provides size) {
                    item.content(TabBarItemScope(selected = selected, enabled = item.enabled))
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
 * @param animationSpec Motion used when the indicator changes tab cells, grows in or shrinks away.
 *   Defaults to the theme's [slide] spring.
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
    animationSpec: FiniteAnimationSpec<Dp> = ArecaTheme.motion.slide(),
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
 * A [Dp] animation spec run on pixel values, so a [FloatingTabBar]'s public `animationSpec` drives
 * the selector's thumb.
 *
 * Both types animate as a one-dimensional vector, so the spec's timing carries over unchanged. Only
 * the values a spec names itself — a keyframe's value, a spring's visibility threshold — are read as
 * pixels rather than dp.
 *
 * @property spec Spec to run.
 */
private data class DpSpecInPixels(private val spec: FiniteAnimationSpec<Dp>) : FiniteAnimationSpec<Float> {
    override fun <V : AnimationVector> vectorize(
        converter: TwoWayConverter<Float, V>,
    ): VectorizedFiniteAnimationSpec<V> {
        @Suppress("UNCHECKED_CAST")
        return spec.vectorize(Dp.VectorConverter as TwoWayConverter<Dp, V>)
    }
}
