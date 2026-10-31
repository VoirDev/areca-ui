package dev.voir.areca.foundation.adaptive

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * One top-level destination of an app: a tab in the bottom bar, an item of a rail or a sidebar.
 *
 * @property value Identifies the destination; reported when it is chosen.
 * @property label The destination's name, shown under or beside its icon and read by assistive
 *   technology.
 * @property icon Glyph standing for the destination.
 * @property enabled Whether the destination can be chosen.
 */
@Immutable
public class NavDestination<T : Any>(
    public val value: T,
    public val label: String,
    public val icon: ImageVector,
    public val enabled: Boolean = true,
)

/**
 * Colours of a [NavRail].
 *
 * @property bg Fill behind the rail.
 * @property indicator Fill of the pill behind the selected destination's icon.
 * @property content Icon and label colour of a destination at rest.
 * @property contentSelected Icon and label colour of the selected destination.
 * @property contentDisabled Icon and label colour of a destination that cannot be chosen.
 * @property indicatorShape Shape of the selection pill and of the press feedback.
 */
@Immutable
public data class NavRailStyle(
    public val bg: Color,
    public val indicator: Color,
    public val content: Color,
    public val contentSelected: Color,
    public val contentDisabled: Color,
    public val indicatorShape: Shape,
) {
    /** The theme's rail. */
    public companion object {
        /** On the canvas, the selected destination on a brand-muted pill. */
        public val Default: NavRailStyle
            @Composable
            get() = default()

        /**
         * Builds a rail style from the theme.
         *
         * @param bg Fill behind the rail.
         * @param indicator Fill of the selection pill.
         * @param content Colour of a destination at rest.
         * @param contentSelected Colour of the selected destination.
         * @param contentDisabled Colour of a destination that cannot be chosen.
         * @param indicatorShape Shape of the pill and the press feedback.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgCanvas,
            indicator: Color = ArecaTheme.colors.bgBrandMuted,
            content: Color = ArecaTheme.colors.fgMuted,
            contentSelected: Color = ArecaTheme.colors.fgBrandText,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            indicatorShape: Shape = ArecaTheme.shapes.full,
        ): NavRailStyle = NavRailStyle(bg, indicator, content, contentSelected, contentDisabled, indicatorShape)
    }
}

/**
 * Dimensions of a [NavRail].
 *
 * @property width Width of the rail.
 * @property verticalPadding Space above the header and below the footer.
 * @property headerGap Space between the header and the first destination.
 * @property itemSpacing Space between destinations.
 * @property indicatorWidth Width of the selection pill.
 * @property indicatorHeight Height of the selection pill.
 * @property icon Size of a destination's icon.
 * @property labelGap Space between the pill and the label.
 * @property labelStyle Type of a destination's label.
 */
@Immutable
public data class NavRailSize(
    public val width: Dp,
    public val verticalPadding: Dp,
    public val headerGap: Dp,
    public val itemSpacing: Dp,
    public val indicatorWidth: Dp,
    public val indicatorHeight: Dp,
    public val icon: Dp,
    public val labelGap: Dp,
    public val labelStyle: TextStyle,
) {
    /** The theme's rail. */
    public companion object {
        /** 80dp wide with a 56 × 32dp pill under touch; 72dp with a 48 × 28dp pill under a pointer. */
        public val Default: NavRailSize
            @Composable
            get() = default()

        /**
         * Builds a rail size from the theme, for the input in scope.
         *
         * @param width Width of the rail.
         * @param verticalPadding Space above the header and below the footer.
         * @param headerGap Space between the header and the first destination.
         * @param itemSpacing Space between destinations.
         * @param indicatorWidth Width of the selection pill.
         * @param indicatorHeight Height of the selection pill.
         * @param icon Size of a destination's icon.
         * @param labelGap Space between the pill and the label.
         * @param labelStyle Type of a destination's label.
         * @return The size.
         */
        @Composable
        public fun default(
            width: Dp = ArecaTheme.sizing.select(touch = 80.dp, pointer = 72.dp),
            verticalPadding: Dp = ArecaTheme.sizing.select(touch = ArecaTheme.spacing.lg, pointer = ArecaTheme.spacing.md),
            headerGap: Dp = ArecaTheme.spacing.xl,
            itemSpacing: Dp = ArecaTheme.sizing.select(touch = ArecaTheme.spacing.md, pointer = ArecaTheme.spacing.sm),
            indicatorWidth: Dp = ArecaTheme.sizing.select(touch = 56.dp, pointer = 48.dp),
            indicatorHeight: Dp = ArecaTheme.sizing.select(touch = 32.dp, pointer = 28.dp),
            icon: Dp = ArecaTheme.sizing.select(touch = IconSize.Medium, pointer = IconSize.Small),
            labelGap: Dp = ArecaTheme.spacing.xs,
            labelStyle: TextStyle = ArecaTheme.typography.labelSmall,
        ): NavRailSize = NavRailSize(
            width, verticalPadding, headerGap, itemSpacing, indicatorWidth, indicatorHeight, icon, labelGap, labelStyle,
        )
    }
}

/** Default [NavRailStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNavRailStyle: ProvidableCompositionLocal<NavRailStyle?> = compositionLocalOf { null }

/** Default [NavRailSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNavRailSize: ProvidableCompositionLocal<NavRailSize?> = compositionLocalOf { null }

/**
 * A narrow column of an app's top-level destinations along the start edge: an icon on a pill over
 * a short label.
 *
 * The middle step between a phone's bottom bar and a desktop's sidebar — a tablet, an unfolded
 * foldable, a phone on its side. [AdaptiveNavigation] chooses it for those windows; draw it
 * yourself for a layout of your own. Destinations are announced as tabs, the selected one as
 * selected. A divider runs along the rail's end edge.
 *
 * ```kotlin
 * NavRail(
 *     destinations = destinations,
 *     selectedValue = tab,
 *     onValueSelected = { tab = it },
 *     header = { IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = add) },
 * )
 * ```
 *
 * @param destinations The destinations, top to bottom.
 * @param selectedValue Value of the selected destination, or `null` for none.
 * @param onValueSelected Called with a destination's value when it is chosen, selected or not.
 * @param modifier Modifier applied to the rail.
 * @param style Colours. Defaults to [LocalNavRailStyle], then [NavRailStyle.Default].
 * @param size Dimensions. Defaults to [LocalNavRailSize], then [NavRailSize.Default].
 * @param showLabels Whether to draw labels under the icons. Turn them off on a short window, where a
 *   labelled rail would not fit; the labels are still announced.
 * @param windowInsets Insets the rail keeps its destinations clear of: the system bars and the
 *   display cutout on the start side by default.
 * @param header Content above the destinations, such as the screen's primary action.
 * @param footer Content at the bottom of the rail, such as an account button.
 */
@Composable
public fun <T : Any> NavRail(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: NavRailStyle = LocalNavRailStyle.current ?: NavRailStyle.Default,
    size: NavRailSize = LocalNavRailSize.current ?: NavRailSize.Default,
    showLabels: Boolean = true,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start),
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Row(modifier = modifier.fillMaxHeight().background(style.bg)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(size.width)
                .windowInsetsPadding(windowInsets)
                .padding(vertical = size.verticalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (header != null) {
                header()
                Spacer(Modifier.height(size.headerGap))
            }
            Column(
                modifier = Modifier.selectableGroup(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(size.itemSpacing),
            ) {
                for (destination in destinations) {
                    NavRailEntry(
                        destination = destination,
                        selected = destination.value == selectedValue,
                        onClick = { onValueSelected(destination.value) },
                        style = style,
                        size = size,
                        showLabel = showLabels,
                    )
                }
            }
            if (footer != null) {
                Spacer(Modifier.weight(1f))
                footer()
            }
        }
        VerticalDivider()
    }
}

/**
 * One destination of a rail: the icon on its pill, then the label.
 *
 * @param destination The destination.
 * @param selected Whether it is the selected one.
 * @param onClick Called when it is chosen.
 * @param style Colours.
 * @param size Dimensions.
 * @param showLabel Whether to draw the label.
 */
@Composable
private fun <T : Any> NavRailEntry(
    destination: NavDestination<T>,
    selected: Boolean,
    onClick: () -> Unit,
    style: NavRailStyle,
    size: NavRailSize,
    showLabel: Boolean,
) {
    val source = remember { MutableInteractionSource() }
    val content by animateColorAsState(
        targetValue = when {
            !destination.enabled -> style.contentDisabled
            selected -> style.contentSelected
            else -> style.content
        },
        label = "navRailContent",
    )
    val indicator by animateColorAsState(
        targetValue = if (selected) style.indicator else Color.Transparent,
        label = "navRailIndicator",
    )

    Column(
        modifier = Modifier
            .width(size.width)
            .selectable(
                selected = selected,
                enabled = destination.enabled,
                role = Role.Tab,
                interactionSource = source,
                // The feedback is drawn on the pill, not across the whole entry.
                indication = null,
                onClick = onClick,
            )
            .clickCursor(destination.enabled),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(size.labelGap),
    ) {
        Box(
            modifier = Modifier
                .size(width = size.indicatorWidth, height = size.indicatorHeight)
                .background(indicator, style.indicatorShape)
                .indication(source, TouchIndication(shape = style.indicatorShape, color = ArecaTheme.colors.fgBase)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = destination.icon,
                // Without a label the icon is the only thing to read.
                contentDescription = if (showLabel) null else destination.label,
                tint = content,
                size = size.icon,
            )
        }
        if (showLabel) {
            Text(
                text = destination.label,
                style = size.labelStyle,
                color = content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
