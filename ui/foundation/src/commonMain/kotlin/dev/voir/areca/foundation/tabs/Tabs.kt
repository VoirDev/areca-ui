package dev.voir.areca.foundation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
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
 * the selected tab a fill of its own, which reads as a segmented control. Use [Underline] to switch
 * views within a screen, [Button] to switch a setting.
 */
public enum class TabsVariant {
    /** The selected tab wears a filled background; no divider or indicator is drawn. */
    Button,

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
        ): TabsStyle = TabsStyle(
            content = content,
            contentSelected = contentSelected,
            contentDisabled = contentDisabled,
            bg = bg,
            bgSelected = bgSelected,
            bgDisabled = bgDisabled,
            divider = divider,
            indicator = indicator,
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
 * @property indicatorDuration Time the indicator takes to travel between tabs.
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
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default. */
        public val Default: TabsSize
            @Composable
            get() = default()

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
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            gap: Dp = ArecaTheme.spacing.xs,
            itemPaddingHorizontal: Dp = ArecaTheme.spacing.md,
            itemPaddingVertical: Dp = ArecaTheme.spacing.sm,
            itemMinHeight: Dp = 36.dp,
            itemMinWidth: Dp = 36.dp,
            itemShape: Shape = ArecaTheme.shapes.md,
            dividerThickness: Dp = 1.dp,
            indicatorThickness: Dp = 2.dp,
            iconSize: Dp = IconSize.Small,
            textStyle: TextStyle = ArecaTheme.typography.labelMedium,
            indicatorDuration: Duration = ArecaTheme.motion.medium,
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
 * The indicator animates between the tabs' *measured* bounds rather than assuming equal widths, so
 * it stays correct when labels differ in length or the strip overflows. The indicator starts under
 * the selected tab rather than travelling there from the strip's edge on the first frame. A strip
 * that overflows scrolls the selected tab into view, in either reading direction, including when
 * the selection changes from code.
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
) {
    val horizontal = orientation == TabsOrientation.Horizontal
    val scrollState = rememberScrollState()
    val selectedIndex = tabs.indexOfFirst { it.value == selectedValue }
    // A horizontal scroll counts from the start edge, which is the right edge in a right-to-left
    // layout, while measured bounds count from the left.
    val reversed = horizontal && LocalLayoutDirection.current == LayoutDirection.Rtl

    // Measured rather than derived: tabs have different widths, and equal-width maths puts the
    // indicator under the wrong label as soon as one label is longer than the others.
    val tabBounds = remember { mutableStateMapOf<Int, TabBounds>() }
    var viewportExtent by remember { mutableIntStateOf(0) }
    var contentExtent by remember { mutableIntStateOf(0) }
    val selectedBounds = tabBounds[selectedIndex]

    val indicator = remember { TabsIndicator() }
    val indicatorSpec = size.indicatorDuration.tween<Float>(ArecaTheme.motion.standard)
    LaunchedEffect(selectedBounds) {
        indicator.moveTo(bounds = selectedBounds, animationSpec = indicatorSpec)
    }

    // Keeps the selected tab visible, including when the selection changes from code rather than
    // from a press.
    LaunchedEffect(selectedBounds, viewportExtent, contentExtent, reversed) {
        val bounds = selectedBounds ?: return@LaunchedEffect

        val target = TabsScrollMath.scrollToReveal(
            tabStart = bounds.start,
            tabEnd = bounds.end,
            viewportExtent = viewportExtent,
            contentExtent = contentExtent,
            scroll = scrollState.value,
            reversed = reversed,
        )
        if (target != null) {
            scrollState.animateScrollTo(target)
        }
    }

    val stripModifier = modifier
        .onSizeChanged { viewportExtent = if (horizontal) it.width else it.height }
        .then(
            if (horizontal) {
                Modifier.horizontalScroll(scrollState)
            } else {
                Modifier.verticalScroll(scrollState)
            }
        )
        // After the scroll, so this is the content's full length rather than the visible window.
        .onSizeChanged { contentExtent = if (horizontal) it.width else it.height }
        .then(
            if (variant == TabsVariant.Underline) {
                Modifier.drawTabsLine(
                    style = style,
                    size = size,
                    selectedBounds = indicator::bounds,
                    horizontal = horizontal,
                    atStartEdge = if (horizontal) {
                        horizontalPosition == TabsHorizontalPosition.Top
                    } else {
                        verticalPosition == TabsVerticalPosition.Start
                    },
                )
            } else {
                Modifier
            }
        )
        .selectableGroup()

    val content: @Composable () -> Unit = {
        tabs.forEachIndexed { index, tab ->
            Tab(
                tab = tab,
                selected = tab.value == selectedValue,
                onSelect = { onValueSelected(tab.value) },
                style = style,
                size = size,
                variant = variant,
                orientation = orientation,
                modifier = Modifier.onGloballyPositioned {
                    val position = it.positionInParent()
                    tabBounds[index] = if (horizontal) {
                        TabBounds(position.x, position.x + it.size.width)
                    } else {
                        TabBounds(position.y, position.y + it.size.height)
                    }
                },
            )
        }
    }

    if (horizontal) {
        Row(
            modifier = stripModifier,
            horizontalArrangement = Arrangement.spacedBy(size.gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    } else {
        Column(
            modifier = stripModifier,
            verticalArrangement = Arrangement.spacedBy(size.gap),
            horizontalAlignment = Alignment.Start,
        ) {
            content()
        }
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
    )
}

/**
 * One tab of a strip.
 *
 * Uses `selectable` rather than `clickable`, so the tab's selected state is announced as part of
 * the enclosing `selectableGroup`.
 *
 * @param tab The tab.
 * @param selected Whether it is the selected one.
 * @param onSelect Called when the user selects it.
 * @param style Colors to draw with.
 * @param size Metrics to draw with.
 * @param variant How much surface the strip draws.
 * @param orientation Which way the strip runs, handed to the tab's content.
 * @param modifier Modifier applied to the tab, carrying its bounds reporting.
 */
@Composable
private fun <T> Tab(
    tab: TabItem<T>,
    selected: Boolean,
    onSelect: () -> Unit,
    style: TabsStyle,
    size: TabsSize,
    variant: TabsVariant,
    orientation: TabsOrientation,
    modifier: Modifier = Modifier,
) {
    val contentColor = when {
        !tab.enabled -> style.contentDisabled
        selected -> style.contentSelected
        else -> style.content
    }
    val backgroundColor = when {
        variant != TabsVariant.Button -> Color.Transparent
        !tab.enabled -> style.bgDisabled
        selected -> style.bgSelected
        else -> style.bg
    }

    val interactionSource = remember { MutableInteractionSource() }
    // A button tab already has a fill to tint; an underline tab has none, so it fades instead.
    val indication = if (variant == TabsVariant.Button) {
        TouchIndication(shape = size.itemShape, color = style.contentSelected)
    } else {
        FadeIndication(shape = size.itemShape)
    }

    CompositionLocalProvider(
        LocalTextStyle provides size.textStyle,
        LocalContentColor provides contentColor,
        LocalIconSize provides size.iconSize,
    ) {
        Box(
            modifier = modifier
                .defaultMinSize(minWidth = size.itemMinWidth, minHeight = size.itemMinHeight)
                // Shaped fill rather than a clip, which would crop the focus ring the indication
                // draws outside the tab.
                .background(backgroundColor, size.itemShape)
                .selectable(
                    selected = selected,
                    enabled = tab.enabled,
                    interactionSource = interactionSource,
                    indication = indication,
                    role = Role.Tab,
                    onClick = { if (!selected) onSelect() },
                )
                .clickCursor(tab.enabled)
                .padding(
                    horizontal = size.itemPaddingHorizontal,
                    vertical = size.itemPaddingVertical,
                ),
            contentAlignment = Alignment.Center,
        ) {
            tab.content(
                TabItemScope(
                    selected = selected,
                    enabled = tab.enabled,
                    orientation = orientation,
                )
            )
        }
    }
}

/**
 * Draws the strip's divider and the indicator sliding along it.
 *
 * One implementation for both orientations: the axis decides which coordinate is fixed and which
 * runs, and nothing else differs. The indicator is drawn over the divider on the same line, so the
 * two read as one rule with a lit segment.
 *
 * @param style Colors to draw with.
 * @param size Thicknesses to draw with.
 * @param selectedBounds Reads where the indicator currently sits, or `null` when nothing is
 *   selected. Called only while drawing, so a moving indicator redraws without recomposing.
 * @param horizontal Whether the strip runs across rather than down.
 * @param atStartEdge Whether the line sits at the top (horizontal) or at the start edge in reading
 *   order (vertical) rather than the opposite one.
 * @return Modifier drawing the line over the content.
 */
private fun Modifier.drawTabsLine(
    style: TabsStyle,
    size: TabsSize,
    selectedBounds: () -> TabBounds?,
    horizontal: Boolean,
    atStartEdge: Boolean,
): Modifier = drawWithContent {
    drawContent()

    // The start edge of a vertical strip is its right edge in a right-to-left layout.
    val atLeftOrTop = horizontal && atStartEdge ||
        !horizontal && atStartEdge == (layoutDirection == LayoutDirection.Ltr)
    val crossExtent = if (horizontal) this.size.height else this.size.width
    val fixed = if (atLeftOrTop) 0f else crossExtent
    val length = if (horizontal) this.size.width else this.size.height

    fun offsetAt(along: Float): Offset =
        if (horizontal) Offset(along, fixed) else Offset(fixed, along)

    drawLine(
        color = style.divider,
        start = offsetAt(0f),
        end = offsetAt(length),
        strokeWidth = size.dividerThickness.toPx(),
    )

    val bounds = selectedBounds() ?: return@drawWithContent

    drawLine(
        color = style.indicator,
        start = offsetAt(bounds.start),
        end = offsetAt(bounds.end),
        strokeWidth = size.indicatorThickness.toPx(),
        cap = StrokeCap.Round,
    )
}
