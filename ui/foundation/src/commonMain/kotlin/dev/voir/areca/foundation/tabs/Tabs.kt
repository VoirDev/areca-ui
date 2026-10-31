package dev.voir.areca.foundation.tabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.selection.SelectorCore
import dev.voir.areca.foundation.selection.SelectorEdge
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SelectorOrientation
import dev.voir.areca.foundation.selection.SelectorRole
import dev.voir.areca.foundation.selection.SelectorThumbKind
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.selection.SlidingSelectorSize
import dev.voir.areca.foundation.selection.SlidingSelectorStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.slide
import dev.voir.areca.theme.tween
import kotlin.time.Duration

/**
 * Which way a tab strip runs.
 *
 * The orientation decides everything downstream: the layout, the axis that scrolls when the tabs
 * overflow, and which edge the underline is drawn along.
 */
public enum class TabsOrientation {
    /** A row of tabs that scrolls sideways when it overflows. */
    Horizontal,

    /** A column of tabs that scrolls vertically when it overflows. Suits a sidebar. */
    Vertical,
}

/** Which long edge of a horizontal tab strip carries the divider and the indicator. */
public enum class TabsHorizontalPosition {
    /** Above the tabs, for a strip at the bottom of a panel. */
    Top,

    /** Below the tabs. The convention. */
    Bottom,
}

/** Which long edge of a vertical tab strip carries the divider and the indicator. */
public enum class TabsVerticalPosition {
    /** Before the tabs in reading order, for a strip left of its content in a left-to-right locale. */
    Start,

    /** After the tabs in reading order. */
    End,
}

/**
 * How much surface a tab strip draws.
 *
 * The two are not interchangeable. [Underline] keeps the tabs on one continuous line and moves a
 * single indicator between them, which reads as one control with a current position. [Button] gives
 * the selected tab a fill of its own, which reads as a segmented control. [Segmented] sets the tabs
 * in a capsule track and slides a raised thumb to the selected one — the same meaning as [Button],
 * drawn as one object. Use [Underline] to switch views within a screen, [Button] or [Segmented] to
 * switch a setting.
 */
public enum class TabsVariant {
    /**
     * The selected tab wears a flat fill, which slides to the newly selected tab. No track, divider
     * or underline is drawn.
     */
    Button,

    /**
     * The tabs sit in a [track][TabsStyle.track] and a raised [thumb][TabsStyle.thumb] slides to
     * the selected one. No divider or underline is drawn.
     */
    Segmented,

    /** A divider runs the strip's length, with an indicator that slides to the selected tab. */
    Underline,
}

/**
 * One tab of a [Tabs] strip.
 *
 * @property value What this tab stands for. Must be unique within its strip, and is what
 *   `onValueSelected` reports.
 * @property enabled Whether the tab can be selected.
 * @property content The tab's label, given its current state.
 */
@Immutable
public data class TabItem<T>(
    public val value: T,
    public val enabled: Boolean = true,
    public val content: @Composable TabItemScope.() -> Unit,
)

/**
 * State of a [TabItem] while its content is being composed.
 *
 * [orientation] is included because a tab's content often has to change with it — an icon above a
 * label in a horizontal strip, beside it in a vertical one.
 *
 * @property selected Whether this tab is the selected one.
 * @property enabled Whether this tab can be selected.
 * @property orientation Which way the strip runs.
 */
@Immutable
public data class TabItemScope(
    public val selected: Boolean,
    public val enabled: Boolean,
    public val orientation: TabsOrientation,
)

/**
 * Colors of a tab strip.
 *
 * Content colors reach a tab's own content through [LocalContentColor], so a plain [Text] or [Icon]
 * inside a tab is colored correctly without being told.
 *
 * Named like every other tab family — [dev.voir.areca.foundation.tabbar.TabBarStyle] and
 * [dev.voir.areca.foundation.tabbar.FloatingTabBarStyle] — with `content` and `bg` for the
 * foreground and the fill, and a `Selected` or `Disabled` suffix for the state.
 *
 * @property content Label color of an unselected tab.
 * @property contentSelected Label color of the selected tab.
 * @property contentDisabled Label color of a tab that cannot be selected.
 * @property bg Fill behind an unselected tab, in [TabsVariant.Button].
 * @property bgSelected Fill behind the selected tab, in [TabsVariant.Button].
 * @property bgDisabled Fill behind a disabled tab, in [TabsVariant.Button].
 * @property divider Line running the strip's length, in [TabsVariant.Underline].
 * @property indicator Mark that slides to the selected tab, in [TabsVariant.Underline].
 * @property track Ground the tabs sit in, in [TabsVariant.Segmented].
 * @property thumb Raised fill that slides to the selected tab, in [TabsVariant.Segmented].
 */
@Immutable
public data class TabsStyle(
    public val content: Color,
    public val contentSelected: Color,
    public val contentDisabled: Color,
    public val bg: Color,
    public val bgSelected: Color,
    public val bgDisabled: Color,
    public val divider: Color,
    public val indicator: Color,
    public val track: Color = Color.Transparent,
    public val thumb: Color = Color.Transparent,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default. */
        public val Default: TabsStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param content Label color of an unselected tab.
         * @param contentSelected Label color of the selected tab.
         * @param contentDisabled Label color of a disabled tab.
         * @param bg Fill behind an unselected tab, in [TabsVariant.Button].
         * @param bgSelected Fill behind the selected tab, in [TabsVariant.Button].
         * @param bgDisabled Fill behind a disabled tab, in [TabsVariant.Button].
         * @param divider Line running the strip's length.
         * @param indicator Mark that slides to the selected tab.
         * @param track Ground the tabs sit in, in [TabsVariant.Segmented].
         * @param thumb Raised fill under the selected tab, in [TabsVariant.Segmented].
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            content: Color = ArecaTheme.colors.fgMuted,
            contentSelected: Color = ArecaTheme.colors.fgBase,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            bg: Color = Color.Transparent,
            bgSelected: Color = ArecaTheme.colors.bgMuted,
            bgDisabled: Color = Color.Transparent,
            divider: Color = ArecaTheme.colors.borderBase,
            indicator: Color = ArecaTheme.colors.fgBase,
            track: Color = ArecaTheme.colors.bgNeutralMuted,
            // Raised means lighter in both themes: white on the light track, and a step lighter than
            // the track in dark, where the surface colour sits darker than it.
            thumb: Color = if (ArecaTheme.isDark) ArecaTheme.colors.bgNeutralBase else ArecaTheme.colors.bgSurface,
        ): TabsStyle = TabsStyle(
            content = content,
            contentSelected = contentSelected,
            contentDisabled = contentDisabled,
            bg = bg,
            bgSelected = bgSelected,
            bgDisabled = bgDisabled,
            divider = divider,
            indicator = indicator,
            track = track,
            thumb = thumb,
        )
    }
}

/**
 * Metrics of a tab strip.
 *
 * [itemMinWidth] and [itemMinHeight] keep a one-character tab from collapsing below a usable target,
 * and make a row of tabs with labels of different lengths look deliberate.
 *
 * @property gap Space between tabs.
 * @property itemPaddingHorizontal Horizontal padding inside a tab.
 * @property itemPaddingVertical Vertical padding inside a tab.
 * @property itemMinHeight Smallest height of a tab.
 * @property itemMinWidth Smallest width of a tab.
 * @property itemShape Shape of a tab's fill and of its press feedback.
 * @property dividerThickness Thickness of the line running the strip's length.
 * @property indicatorThickness Thickness of the mark that slides to the selected tab.
 * @property iconSize Size given to icons inside a tab.
 * @property textStyle Style given to a tab's label.
 * @property indicatorDuration Time the indicator, or the segmented thumb, takes to travel between
 *   tabs. Left at the theme's `motion.medium`, the indicator moves on the theme's
 *   [slide][dev.voir.areca.theme.slide] spring instead, like every
 *   [SlidingSelector][dev.voir.areca.foundation.selection.SlidingSelector]; any other duration is
 *   a tween of that length.
 * @property trackPadding Space between the track's edge and the tabs, in [TabsVariant.Segmented].
 * @property thumbElevation Shadow under the thumb, in [TabsVariant.Segmented].
 */
@Immutable
public data class TabsSize(
    public val gap: Dp,
    public val itemPaddingHorizontal: Dp,
    public val itemPaddingVertical: Dp,
    public val itemMinHeight: Dp,
    public val itemMinWidth: Dp,
    public val itemShape: Shape,
    public val dividerThickness: Dp,
    public val indicatorThickness: Dp,
    public val iconSize: Dp,
    public val textStyle: TextStyle,
    public val indicatorDuration: Duration,
    public val trackPadding: Dp = 0.dp,
    public val thumbElevation: Dp = 0.dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** 36dp under touch, 30dp under a pointer. Theme default. */
        public val Default: TabsSize
            @Composable
            get() = default()

        /**
         * 32dp under touch, 28dp under a pointer. Metrics for [TabsVariant.Segmented]: capsule tabs
         * a hairline inside their track, with a thumb raised one elevation step.
         */
        public val Segmented: TabsSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return default(
                    gap = spacing.xs2,
                    itemPaddingHorizontal = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    itemPaddingVertical = spacing.xs,
                    itemMinHeight = sizing.controlXs,
                    itemShape = ArecaTheme.shapes.full,
                    trackPadding = spacing.xs2,
                    thumbElevation = ArecaTheme.elevation.xs,
                )
            }

        /**
         * Builds a size from the current theme.
         *
         * @param gap Space between tabs.
         * @param itemPaddingHorizontal Horizontal padding inside a tab.
         * @param itemPaddingVertical Vertical padding inside a tab.
         * @param itemMinHeight Smallest height of a tab.
         * @param itemMinWidth Smallest width of a tab.
         * @param itemShape Shape of a tab's fill and press feedback.
         * @param dividerThickness Thickness of the strip's line.
         * @param indicatorThickness Thickness of the sliding indicator.
         * @param iconSize Size given to icons inside a tab.
         * @param textStyle Style given to a tab's label.
         * @param indicatorDuration Time the indicator takes to travel between tabs.
         * @param trackPadding Space between the track's edge and the tabs, in
         *   [TabsVariant.Segmented].
         * @param thumbElevation Shadow under the thumb, in [TabsVariant.Segmented].
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            gap: Dp = ArecaTheme.sizing.select(
                touch = ArecaTheme.spacing.xs,
                pointer = ArecaTheme.spacing.xs2,
            ),
            itemPaddingHorizontal: Dp = ArecaTheme.sizing.select(
                touch = ArecaTheme.spacing.md,
                pointer = ArecaTheme.spacing.sm,
            ),
            itemPaddingVertical: Dp = ArecaTheme.sizing.select(
                touch = ArecaTheme.spacing.sm,
                pointer = ArecaTheme.spacing.xs,
            ),
            itemMinHeight: Dp = ArecaTheme.sizing.select(touch = 36.dp, pointer = 30.dp),
            itemMinWidth: Dp = ArecaTheme.sizing.select(touch = 36.dp, pointer = 30.dp),
            itemShape: Shape = ArecaTheme.shapes.md,
            dividerThickness: Dp = 1.dp,
            indicatorThickness: Dp = 2.dp,
            iconSize: Dp = IconSize.Small,
            textStyle: TextStyle = ArecaTheme.typography.labelMedium,
            indicatorDuration: Duration = ArecaTheme.motion.medium,
            trackPadding: Dp = 0.dp,
            thumbElevation: Dp = 0.dp,
        ): TabsSize = TabsSize(
            gap = gap,
            itemPaddingHorizontal = itemPaddingHorizontal,
            itemPaddingVertical = itemPaddingVertical,
            itemMinHeight = itemMinHeight,
            itemMinWidth = itemMinWidth,
            itemShape = itemShape,
            dividerThickness = dividerThickness,
            indicatorThickness = indicatorThickness,
            iconSize = iconSize,
            textStyle = textStyle,
            indicatorDuration = indicatorDuration,
            trackPadding = trackPadding,
            thumbElevation = thumbElevation,
        )
    }
}

/**
 * Selection of a locally controlled [Tabs] strip.
 *
 * Use it when the selection belongs to the screen and nothing else. When it belongs to navigation or
 * a view model, use the [Tabs] overload taking `selectedValue` and `onValueSelected` instead.
 *
 * @param initialValue Tab selected on the first composition.
 */
@Stable
public class TabsState<T> internal constructor(initialValue: T) {
    /** Currently selected tab's value. */
    public var selectedValue: T by mutableStateOf(initialValue)
        private set

    /**
     * Selects [value].
     *
     * @param value Tab value to select.
     */
    public fun select(value: T) {
        selectedValue = value
    }

    /**
     * Whether [value] is the selected tab.
     *
     * @param value Tab value to test.
     * @return `true` when it is selected.
     */
    public fun isSelected(value: T): Boolean = selectedValue == value
}

/**
 * Remembers the selection of a locally controlled tab strip.
 *
 * [initialValue] is read once. A later change does not move the selection — the strip belongs to the
 * user from the first composition onwards, and snapping back because a parameter changed would undo
 * their action.
 *
 * The selection is saved, so it survives configuration change and process death, whenever the
 * platform can store the selected value itself: a `String`, a number, an enum constant. Any other
 * value — a sealed-class destination, a plain data class — is kept only for as long as the
 * composition lives, and the strip returns to [initialValue] when it is recreated. Hoist the
 * selection yourself when such a value has to survive.
 *
 * @param initialValue Tab selected on the first composition.
 * @return State remembered for this position in the composition.
 */
@Composable
public fun <T : Any> rememberTabsState(initialValue: T): TabsState<T> = rememberSaveable(
    saver = tabsStateSaver(),
) {
    TabsState(initialValue)
}

/**
 * Saves a [TabsState] as its selected value, when the platform can store that value.
 *
 * A value the registry cannot store is not saved at all rather than handed over, because handing it
 * over makes `rememberSaveable` throw on Android.
 *
 * @return Saver that stores storable selections and skips the rest.
 */
internal fun <T : Any> tabsStateSaver(): Saver<TabsState<T>, T> = Saver(
    save = { state -> state.selectedValue.takeIf { canBeSaved(it) } },
    restore = { TabsState(it) },
)

/** Default [TabsStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTabsStyle: ProvidableCompositionLocal<TabsStyle?> = compositionLocalOf { null }

/** Default [TabsSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTabsSize: ProvidableCompositionLocal<TabsSize?> = compositionLocalOf { null }

/**
 * A strip of tabs, one of which is selected.
 *
 * The selection is the caller's: pass [selectedValue] and handle [onValueSelected]. That is what
 * lets the strip be driven by navigation or a view model rather than owning state a screen also
 * needs. For a strip whose selection belongs to nothing else, use the [TabsState] overload.
 *
 * Built on [SlidingSelector]: each variant is one of its looks, and the indicator is its thumb. The
 * indicator animates between the tabs' *measured* bounds rather than assuming equal widths, so it
 * stays correct when labels differ in length or the strip overflows. It starts under the selected
 * tab rather than travelling there from the strip's edge on the first frame. A strip that overflows
 * scrolls the selected tab into view, in either reading direction, including when the selection
 * changes from code.
 *
 * Announced as a group of tabs: a screen reader reports each tab's selected state, and each enabled
 * tab is its own focus stop, reached with the Tab key and selected with Enter or Space.
 *
 * @param tabs Tabs to show, in order.
 * @param selectedValue Value of the selected tab.
 * @param onValueSelected Called with a tab's value when the user selects it. Never called for a
 *   disabled tab, nor for the tab that is already selected.
 * @param modifier Modifier applied to the strip.
 * @param style Colors. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 * @param variant How much surface the strip draws.
 * @param orientation Which way the strip runs.
 * @param horizontalPosition Edge carrying the divider and indicator, when horizontal.
 * @param verticalPosition Edge carrying the divider and indicator, when vertical.
 * @param fillWidth Whether the tabs share the strip's width equally, horizontally. The strip then
 *   fills the width it is given and does not scroll, so every tab must fit. `false` sizes each tab
 *   to its label.
 */
@Composable
public fun <T> Tabs(
    tabs: List<TabItem<T>>,
    selectedValue: T,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: TabsStyle = LocalTabsStyle.current ?: TabsStyle.Default,
    size: TabsSize = LocalTabsSize.current ?: TabsSize.Default,
    variant: TabsVariant = TabsVariant.Underline,
    orientation: TabsOrientation = TabsOrientation.Horizontal,
    horizontalPosition: TabsHorizontalPosition = TabsHorizontalPosition.Bottom,
    verticalPosition: TabsVerticalPosition = TabsVerticalPosition.Start,
    fillWidth: Boolean = false,
) {
    val horizontal = orientation == TabsOrientation.Horizontal
    val selectedIndex = tabs.indexOfFirst { it.value == selectedValue }
    val selectorStyle = selectorStyleOf(
        style = style,
        size = size,
        variant = variant,
        edge = if (horizontal) {
            if (horizontalPosition == TabsHorizontalPosition.Top) SelectorEdge.Start else SelectorEdge.End
        } else {
            if (verticalPosition == TabsVerticalPosition.Start) SelectorEdge.Start else SelectorEdge.End
        },
    )

    SelectorCore(
        count = tabs.size,
        selectedIndex = selectedIndex,
        isEnabled = { tabs[it].enabled },
        onTap = { index -> if (index != selectedIndex) onValueSelected(tabs[index].value) },
        role = SelectorRole.Tab,
        style = selectorStyle,
        size = SlidingSelectorSize(
            gap = size.gap,
            itemPadding = PaddingValues(
                horizontal = size.itemPaddingHorizontal,
                vertical = size.itemPaddingVertical,
            ),
            itemMinWidth = size.itemMinWidth,
            itemMinHeight = size.itemMinHeight,
            iconSize = size.iconSize,
            textStyle = size.textStyle,
        ),
        layout = if (fillWidth && horizontal) SelectorLayout.Fill else SelectorLayout.Scroll,
        orientation = if (horizontal) SelectorOrientation.Horizontal else SelectorOrientation.Vertical,
        modifier = modifier,
        itemBackground = { _, selected, enabled ->
            when {
                // The selected fill is the thumb, which slides between tabs.
                variant != TabsVariant.Button || selected -> Color.Transparent
                !enabled -> style.bgDisabled
                else -> style.bg
            }
        },
    ) { index, selected ->
        val tab = tabs[index]
        tab.content(TabItemScope(selected = selected, enabled = tab.enabled, orientation = orientation))
    }
}

/**
 * The [SlidingSelector] look a tab strip's variant draws with.
 *
 * [TabsSize.indicatorDuration] keeps its meaning: left at the theme's medium duration, the indicator
 * moves on the theme's [slide] spring like every other selector; set to anything else, it travels on
 * a tween of that length.
 *
 * @param style Colours of the strip.
 * @param size Metrics of the strip.
 * @param variant How much surface the strip draws.
 * @param edge Edge carrying the divider and the underline.
 * @return Style for the selector under the strip.
 */
@Composable
private fun selectorStyleOf(
    style: TabsStyle,
    size: TabsSize,
    variant: TabsVariant,
    edge: SelectorEdge,
): SlidingSelectorStyle {
    val motion = ArecaTheme.motion
    val base = SlidingSelectorStyle.default(
        content = style.content,
        contentSelected = style.contentSelected,
        contentDisabled = style.contentDisabled,
        track = Color.Transparent,
        trackShape = size.itemShape,
        trackPadding = 0.dp,
        thumbShape = size.itemShape,
        thumbElevation = 0.dp,
        thumbThickness = size.indicatorThickness,
        thumbEdge = edge,
        dividerThickness = size.dividerThickness,
        itemShape = size.itemShape,
        // A button or segmented tab already has a fill to tint; an underline tab has none, so it
        // fades instead.
        indication = if (variant != TabsVariant.Underline) {
            TouchIndication(shape = size.itemShape, color = style.contentSelected)
        } else {
            FadeIndication(shape = size.itemShape)
        },
        motion = if (size.indicatorDuration == motion.medium) {
            motion.slide()
        } else {
            size.indicatorDuration.tween(motion.standard)
        },
    )
    return when (variant) {
        TabsVariant.Button -> base.copy(thumb = style.bgSelected)
        TabsVariant.Segmented -> base.copy(
            track = style.track,
            trackPadding = size.trackPadding,
            thumb = style.thumb,
            thumbElevation = size.thumbElevation,
        )
        TabsVariant.Underline -> base.copy(
            thumb = style.indicator,
            thumbKind = SelectorThumbKind.Underline,
            thumbShape = ArecaTheme.shapes.full,
            divider = style.divider,
        )
    }
}

/**
 * A strip of tabs whose selection belongs to the strip.
 *
 * @param tabs Tabs to show, in order.
 * @param state Selection, from [rememberTabsState].
 * @param modifier Modifier applied to the strip.
 * @param style Colors. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 * @param variant How much surface the strip draws.
 * @param orientation Which way the strip runs.
 * @param horizontalPosition Edge carrying the divider and indicator, when horizontal.
 * @param verticalPosition Edge carrying the divider and indicator, when vertical.
 * @param fillWidth Whether the tabs share the strip's width equally, horizontally.
 */
@Composable
public fun <T> Tabs(
    tabs: List<TabItem<T>>,
    state: TabsState<T>,
    modifier: Modifier = Modifier,
    style: TabsStyle = LocalTabsStyle.current ?: TabsStyle.Default,
    size: TabsSize = LocalTabsSize.current ?: TabsSize.Default,
    variant: TabsVariant = TabsVariant.Underline,
    orientation: TabsOrientation = TabsOrientation.Horizontal,
    horizontalPosition: TabsHorizontalPosition = TabsHorizontalPosition.Bottom,
    verticalPosition: TabsVerticalPosition = TabsVerticalPosition.Start,
    fillWidth: Boolean = false,
) {
    Tabs(
        tabs = tabs,
        selectedValue = state.selectedValue,
        onValueSelected = state::select,
        modifier = modifier,
        style = style,
        size = size,
        variant = variant,
        orientation = orientation,
        horizontalPosition = horizontalPosition,
        verticalPosition = verticalPosition,
        fillWidth = fillWidth,
    )
}
