package dev.voir.areca.foundation.adaptive

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.button.focusRing
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.TooltipBox
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.DotsVertical
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.FoldOrientation
import dev.voir.areca.window.rememberFoldView
import dev.voir.areca.window.thickness

/** Which edge of the window an [EdgeColumn] runs along. Start and end follow the layout direction. */
public enum class EdgeColumnSide {
    /**
     * The side the display's camera cutout is on, so the column's top cluster sits under the camera;
     * [End] when the cutout is along the top edge or there is none.
     */
    Auto,

    /** The leading edge: the left one left to right. */
    Start,

    /** The trailing edge: the right one left to right. Where a foldable keeps its camera. */
    End,
}

/**
 * [Start] or [End] for the window in scope: [EdgeColumnSide.Auto] resolved against the display
 * cutout, the others as they are.
 *
 * @return The side the column is drawn on.
 */
@Composable
internal fun EdgeColumnSide.resolve(): EdgeColumnSide {
    if (this != EdgeColumnSide.Auto) return this
    val cutout = WindowInsets.displayCutout
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val left = cutout.getLeft(density, direction)
    val right = cutout.getRight(density, direction)
    val start = if (direction == LayoutDirection.Ltr) left else right
    val end = if (direction == LayoutDirection.Ltr) right else left
    return if (start > end) EdgeColumnSide.Start else EdgeColumnSide.End
}

/**
 * The insets a column on [side] keeps clear of: the system bars and the display cutout, vertically
 * and on its own side.
 *
 * @param side The column's side; [EdgeColumnSide.Auto] is resolved first.
 * @return The insets.
 */
@Composable
internal fun edgeColumnInsets(side: EdgeColumnSide): WindowInsets = WindowInsets.safeDrawing.only(
    WindowInsetsSides.Vertical + if (side.resolve() == EdgeColumnSide.Start) WindowInsetsSides.Start else WindowInsetsSides.End,
)

/** How loud an [EdgeButton] is. */
public enum class EdgeButtonEmphasis {
    /** A floating circle on the column's surface: most toolbar actions. */
    Plain,

    /** Filled with the brand: the screen's one primary action. */
    Primary,

    /** Filled with the contrast fill: a switch that is on, such as an open sidebar. */
    Toggled,
}

/**
 * Colours of an [EdgeColumn] and the parts drawn in it: [EdgeButton], [VerticalTabBar],
 * [EdgeStatus] and [FloatingSidebar].
 *
 * @property surface Fill of a floating button, the tab capsule and the sidebar panel.
 * @property border Hairline around those surfaces, so they read on a background of the same fill.
 * @property content Icon colour on [surface].
 * @property contentSelected Icon colour of the selected destination.
 * @property contentDisabled Icon colour of a destination or action that cannot be chosen.
 * @property indicator Fill behind the selected destination's icon.
 * @property toggled Fill of an [EdgeButtonEmphasis.Toggled] button.
 * @property onToggled Icon colour on [toggled].
 * @property attention Fill behind an [EdgeStatus] that needs attention.
 * @property onAttention Icon colour on [attention].
 * @property scrim Fill over the content while the sidebar is open.
 * @property panelShape Shape of the sidebar panel.
 * @property shadowElevation How far the floating surfaces float above the content.
 */
@Immutable
public data class EdgeColumnStyle(
    public val surface: Color,
    public val border: Color,
    public val content: Color,
    public val contentSelected: Color,
    public val contentDisabled: Color,
    public val indicator: Color,
    public val toggled: Color,
    public val onToggled: Color,
    public val attention: Color,
    public val onAttention: Color,
    public val scrim: Color,
    public val panelShape: Shape,
    public val shadowElevation: Dp,
) {
    /** The theme's column. */
    public companion object {
        /** Surface circles with a hairline, the selected tab on a brand-muted pill. */
        public val Default: EdgeColumnStyle
            @Composable
            get() = default()

        /**
         * Builds a column style from the theme.
         *
         * @param surface Fill of the floating surfaces.
         * @param border Hairline around them.
         * @param content Icon colour on [surface].
         * @param contentSelected Icon colour of the selected destination.
         * @param contentDisabled Icon colour of something that cannot be chosen.
         * @param indicator Fill behind the selected destination.
         * @param toggled Fill of a toggled button.
         * @param onToggled Icon colour on [toggled].
         * @param attention Fill behind a status that needs attention.
         * @param onAttention Icon colour on [attention].
         * @param scrim Fill over the content while the sidebar is open.
         * @param panelShape Shape of the sidebar panel.
         * @param shadowElevation How far the floating surfaces float.
         * @return The style.
         */
        @Composable
        public fun default(
            surface: Color = ArecaTheme.colors.bgSurface,
            border: Color = ArecaTheme.colors.borderStrong,
            content: Color = ArecaTheme.colors.fgBase,
            contentSelected: Color = ArecaTheme.colors.fgBrandText,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            indicator: Color = ArecaTheme.colors.bgBrandMuted,
            toggled: Color = ArecaTheme.colors.bgContrast,
            onToggled: Color = ArecaTheme.colors.fgOnContrast,
            attention: Color = ArecaTheme.colors.bgWarnMuted,
            onAttention: Color = ArecaTheme.colors.fgWarnText,
            scrim: Color = ArecaTheme.colors.bgOverlayDark,
            panelShape: Shape = ArecaTheme.shapes.xl,
            shadowElevation: Dp = ArecaTheme.elevation.lg,
        ): EdgeColumnStyle = EdgeColumnStyle(
            surface, border, content, contentSelected, contentDisabled, indicator, toggled, onToggled,
            attention, onAttention, scrim, panelShape, shadowElevation,
        )
    }
}

/**
 * Dimensions of an [EdgeColumn] and the parts drawn in it.
 *
 * @property inset Space between the window's safe edge and the column, and above and below it.
 * @property width Width of the column: a button and the capsule's padding on either side.
 * @property button Diameter of an [EdgeButton] and of a tab in the [VerticalTabBar].
 * @property icon Size of the icon in a button or a tab.
 * @property spacing Space between the parts of one cluster.
 * @property capsulePadding Space between the tab capsule's edge and its tabs.
 * @property status Diameter of an [EdgeStatus].
 * @property statusIcon Size of the icon in an [EdgeStatus].
 * @property contentGap Space between the column and the content's text and controls.
 * @property maxTabs Most tabs the capsule shows; more fold into its More item.
 * @property maxToolbarButtons Most round buttons a screen's actions take at the foot of the column
 *   under [dev.voir.areca.window.ActionReach.Thumb], More included; more fold into More.
 */
@Immutable
public data class EdgeColumnSize(
    public val inset: Dp,
    public val width: Dp,
    public val button: Dp,
    public val icon: Dp,
    public val spacing: Dp,
    public val capsulePadding: Dp,
    public val status: Dp,
    public val statusIcon: Dp,
    public val contentGap: Dp,
    public val maxTabs: Int,
    public val maxToolbarButtons: Int,
) {
    /** How far from its edge the content keeps its text and controls: the inset, the column and the gap. */
    public val clearance: Dp get() = inset + width + contentGap

    /** The theme's column. */
    public companion object {
        /** 48dp buttons in a 56dp column under touch; 40dp in 48dp under a pointer. */
        public val Default: EdgeColumnSize
            @Composable
            get() = default()

        /**
         * Builds a column size from the theme, for the input in scope.
         *
         * @param inset Space between the safe edge and the column.
         * @param button Diameter of a button and of a tab.
         * @param capsulePadding Space between the capsule's edge and its tabs.
         * @param width Width of the column.
         * @param icon Size of an icon in a button or a tab.
         * @param spacing Space between the parts of one cluster.
         * @param status Diameter of a status.
         * @param statusIcon Size of the icon in a status.
         * @param contentGap Space between the column and the content.
         * @param maxTabs Most tabs the capsule shows.
         * @param maxToolbarButtons Most toolbar buttons in the column.
         * @return The size.
         */
        @Composable
        public fun default(
            inset: Dp = ArecaTheme.spacing.lg,
            button: Dp = ArecaTheme.sizing.select(touch = 48.dp, pointer = 40.dp),
            capsulePadding: Dp = ArecaTheme.spacing.xs,
            width: Dp = button + capsulePadding * 2,
            icon: Dp = ArecaTheme.sizing.select(touch = IconSize.Medium, pointer = IconSize.Small),
            spacing: Dp = ArecaTheme.spacing.md,
            status: Dp = 28.dp,
            statusIcon: Dp = 16.dp,
            contentGap: Dp = ArecaTheme.spacing.lg,
            maxTabs: Int = 5,
            maxToolbarButtons: Int = 3,
        ): EdgeColumnSize = EdgeColumnSize(
            inset, width, button, icon, spacing, capsulePadding, status, statusIcon, contentGap, maxTabs,
            maxToolbarButtons,
        )
    }
}

/** Default [EdgeColumnStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalEdgeColumnStyle: ProvidableCompositionLocal<EdgeColumnStyle?> = compositionLocalOf { null }

/** Default [EdgeColumnSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalEdgeColumnSize: ProvidableCompositionLocal<EdgeColumnSize?> = compositionLocalOf { null }

/**
 * A floating column of chrome along one edge of the window, in two clusters: [top] under the
 * window's top edge — status, then back — and [bottom] at its foot, within reach of a thumb — the
 * destinations, then search. A window held from below
 * ([dev.voir.areca.window.ActionReach.Thumb]) puts its actions in [bottom] too, between the
 * destinations and search. The middle stays empty and the content shows through.
 *
 * The form [AdaptiveNavigation] draws on a medium window and wider. Draw it yourself for a shell of
 * your own: place it over full-bleed content in a `Box`, aligned to its [side], and keep the
 * content's text and controls [EdgeColumnSize.clearance] from that edge. On a foldable propped up
 * along a horizontal fold, [top] stays above the fold and [bottom] below it. Fill the clusters with
 * [EdgeStatus], [EdgeButton] and [VerticalTabBar].
 *
 * ```kotlin
 * Box(Modifier.fillMaxSize()) {
 *     Screen(Modifier.padding(end = EdgeColumnSize.Default.clearance))
 *     EdgeColumn(
 *         modifier = Modifier.align(Alignment.CenterEnd),
 *         top = { EdgeButton(ArecaIcons.Plus, "New transaction", onClick = add, emphasis = EdgeButtonEmphasis.Primary) },
 *         bottom = { VerticalTabBar(destinations, tab, onValueSelected = { tab = it }) },
 *     )
 * }
 * ```
 *
 * @param modifier Modifier applied to the column.
 * @param side The edge the column runs along, for the insets it keeps clear of; [EdgeColumnSide.Auto]
 *   follows the camera cutout. Align the column to the same edge.
 * @param size Dimensions. Defaults to [LocalEdgeColumnSize], then [EdgeColumnSize.Default].
 * @param windowInsets Insets the column keeps clear of: the system bars and the display cutout on
 *   its side by default.
 * @param top The upper cluster, top down.
 * @param bottom The lower cluster, top down, ending at the window's foot.
 */
@Composable
public fun EdgeColumn(
    modifier: Modifier = Modifier,
    side: EdgeColumnSide = EdgeColumnSide.Auto,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
    windowInsets: WindowInsets = edgeColumnInsets(side),
    top: @Composable ColumnScope.() -> Unit,
    bottom: @Composable ColumnScope.() -> Unit,
) {
    val foldView = rememberFoldView()
    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .windowInsetsPadding(windowInsets)
            .padding(size.inset)
            .then(foldView.modifier)
            .width(size.width),
    ) {
        val fold = foldView.foldIn(DpSize(maxWidth, maxHeight))?.takeIf { it.orientation == FoldOrientation.Horizontal }
        if (fold == null) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(size.spacing),
            ) {
                top()
                // The empty middle: whatever height the clusters leave.
                Spacer(Modifier.weight(1f))
                bottom()
            }
        } else {
            // Propped up on a table: the top cluster stays on the upper half, the bottom cluster
            // on the lower half, and nothing sits on the fold.
            Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    modifier = Modifier.height((fold.bounds.top - size.spacing).coerceAtLeast(0.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(size.spacing),
                    content = top,
                )
                Spacer(Modifier.height(fold.thickness + size.spacing * 2))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(size.spacing, Alignment.Bottom),
                    content = bottom,
                )
            }
        }
    }
}

/**
 * A round action of an [EdgeColumn]: a screen's toolbar action, back, the sidebar toggle, search.
 *
 * Under a pointer it shows [contentDescription] as a tooltip, as [IconButton] does.
 *
 * @param imageVector The action's glyph.
 * @param contentDescription What the action is announced and shown as.
 * @param onClick Called when the action is pressed.
 * @param modifier Modifier applied to the button.
 * @param emphasis How loud the button is.
 * @param enabled Whether the action is available.
 * @param style Colours. Defaults to [LocalEdgeColumnStyle], then [EdgeColumnStyle.Default].
 * @param size Dimensions. Defaults to [LocalEdgeColumnSize], then [EdgeColumnSize.Default].
 */
@Composable
public fun EdgeButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasis: EdgeButtonEmphasis = EdgeButtonEmphasis.Plain,
    enabled: Boolean = true,
    style: EdgeColumnStyle = LocalEdgeColumnStyle.current ?: EdgeColumnStyle.Default,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
) {
    val shape = ArecaTheme.shapes.full
    val buttonStyle = when (emphasis) {
        EdgeButtonEmphasis.Primary -> IconButtonStyle.solid(ActionColor.Brand, shape)
        EdgeButtonEmphasis.Toggled -> edgeButtonStyle(fill = style.toggled, content = style.onToggled, border = Color.Transparent, shape = shape)
        EdgeButtonEmphasis.Plain -> edgeButtonStyle(fill = style.surface, content = style.content, border = style.border, shape = shape)
    }
    IconButton(
        imageVector = imageVector,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier.shadow(style.shadowElevation, shape),
        style = buttonStyle,
        size = IconButtonSize(
            container = size.button,
            icon = size.icon,
            borderWidth = 1.dp,
            spinner = SpinnerSize.Small,
        ),
        enabled = enabled,
    )
}

/**
 * An [EdgeButton]'s fill, icon colour and border, reacting in its own shape.
 *
 * @param fill Container fill.
 * @param content Icon colour.
 * @param border Border colour.
 * @param shape Container shape.
 * @return The icon button style.
 */
@Composable
private fun edgeButtonStyle(fill: Color, content: Color, border: Color, shape: Shape): IconButtonStyle = IconButtonStyle(
    bg = fill,
    border = border,
    content = content,
    contentDisabled = ArecaTheme.colors.fgDisabled,
    shape = shape,
    indication = FadeIndication.tint(
        shape = shape,
        ink = content,
        focusOutline = FocusOutline(color = ActionColor.Brand.focusRing(ArecaTheme.colors)),
    ),
)

/**
 * An app's top-level destinations as a vertical capsule of icons, for the foot of an [EdgeColumn].
 *
 * The first destination is at the top. Destinations are announced as tabs, each by its label, the
 * selected one as selected; under a pointer the label shows as a tooltip. Past
 * [EdgeColumnSize.maxTabs] the last tab is More, which lists the rest in an [AdaptiveMenu] and shows
 * as selected while one of them is.
 *
 * @param destinations The destinations, top to bottom.
 * @param selectedValue Value of the selected destination, or `null` for none.
 * @param onValueSelected Called with a destination's value when it is chosen.
 * @param modifier Modifier applied to the capsule.
 * @param moreLabel What the More tab is announced and shown as.
 * @param style Colours. Defaults to [LocalEdgeColumnStyle], then [EdgeColumnStyle.Default].
 * @param size Dimensions. Defaults to [LocalEdgeColumnSize], then [EdgeColumnSize.Default].
 */
@Composable
public fun <T : Any> VerticalTabBar(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    moreLabel: String = "More",
    style: EdgeColumnStyle = LocalEdgeColumnStyle.current ?: EdgeColumnStyle.Default,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
) {
    val overflows = destinations.size > size.maxTabs
    val shown = if (overflows) destinations.take(size.maxTabs - 1) else destinations
    val folded = if (overflows) destinations.drop(size.maxTabs - 1) else emptyList()
    val shape = ArecaTheme.shapes.full

    Column(
        modifier = modifier
            .shadow(style.shadowElevation, shape)
            .background(style.surface, shape)
            .border(1.dp, style.border, shape)
            .padding(size.capsulePadding)
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(size.capsulePadding),
    ) {
        for (destination in shown) {
            EdgeTab(
                icon = destination.icon,
                label = destination.label,
                selected = destination.value == selectedValue,
                enabled = destination.enabled,
                onClick = { onValueSelected(destination.value) },
                style = style,
                size = size,
            )
        }
        if (folded.isNotEmpty()) {
            val menu = rememberDropdownPopup(placement = AnchoredPlacement.AboveEnd)
            EdgeTab(
                icon = BundledIcons.DotsVertical,
                label = moreLabel,
                selected = folded.any { it.value == selectedValue },
                enabled = true,
                onClick = menu::show,
                style = style,
                size = size,
                modifier = Modifier.anchored(menu.anchorState),
            )
            AdaptiveMenu(
                state = menu,
                title = moreLabel,
                actions = folded.map { destination ->
                    AdaptiveMenuItem(
                        label = destination.label,
                        icon = destination.icon,
                        enabled = destination.enabled,
                        onClick = { onValueSelected(destination.value) },
                    )
                },
            )
        }
    }
}

/**
 * One tab of the capsule: its icon on a pill while selected, its label announced and, under a
 * pointer, shown as a tooltip.
 *
 * @param icon The destination's glyph.
 * @param label The destination's name.
 * @param selected Whether it is the selected one.
 * @param enabled Whether it can be chosen.
 * @param onClick Called when it is chosen.
 * @param style Colours.
 * @param size Dimensions.
 * @param modifier Modifier applied to the tab.
 */
@Composable
private fun EdgeTab(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    style: EdgeColumnStyle,
    size: EdgeColumnSize,
    modifier: Modifier = Modifier,
) {
    val source = remember { MutableInteractionSource() }
    val shape = ArecaTheme.shapes.full
    val content by animateColorAsState(
        targetValue = when {
            !enabled -> style.contentDisabled
            selected -> style.contentSelected
            else -> style.content
        },
        label = "edgeTabContent",
    )
    val indicator by animateColorAsState(
        targetValue = if (selected) style.indicator else Color.Transparent,
        label = "edgeTabIndicator",
    )
    TooltipBox(text = label, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size.button)
                .background(indicator, shape)
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = source,
                    indication = null,
                    onClick = onClick,
                )
                .indication(source, TouchIndication(shape = shape, color = ArecaTheme.colors.fgBase))
                .clickCursor(enabled)
                .semantics { contentDescription = label },
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = content, size = size.icon)
        }
    }
}

/**
 * The app's own status in an [EdgeColumn], under the system's: synced, offline, changes waiting.
 *
 * One icon in a small circle, filled with [EdgeColumnStyle.attention] while [attention]. Say the
 * whole state in [contentDescription], and put its full text where there is room for it — the
 * sidebar's footer.
 *
 * @param imageVector The state's glyph.
 * @param contentDescription The state, as assistive technology reads it.
 * @param modifier Modifier applied to the status.
 * @param attention Whether the state needs the person's attention.
 * @param onClick Called when the status is pressed, to explain or resolve it. `null` makes it
 *   informational.
 * @param style Colours. Defaults to [LocalEdgeColumnStyle], then [EdgeColumnStyle.Default].
 * @param size Dimensions. Defaults to [LocalEdgeColumnSize], then [EdgeColumnSize.Default].
 */
@Composable
public fun EdgeStatus(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    attention: Boolean = false,
    onClick: (() -> Unit)? = null,
    style: EdgeColumnStyle = LocalEdgeColumnStyle.current ?: EdgeColumnStyle.Default,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
) {
    val shape = ArecaTheme.shapes.full
    val press = if (onClick != null) {
        Modifier.clickable(role = Role.Button, onClick = onClick).clickCursor(true)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .size(size.status)
            .background(if (attention) style.attention else Color.Transparent, shape)
            .then(press)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = if (attention) style.onAttention else style.content,
            size = size.statusIcon,
        )
    }
}
