package dev.voir.areca.foundation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalChevronSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Colors of a [NavItem] across its states.
 *
 * Colors come in three groups — the main content (usually the label), the "extra" visuals in the
 * leading and trailing slots (icons, badges, chevrons), and the container — and each group has a
 * resting, a disabled and a selected value. Disabled wins over selected.
 *
 * @property content Main content color at rest.
 * @property contentDisabled Main content color while unavailable.
 * @property contentSelected Main content color while selected.
 * @property extra Leading and trailing slot color at rest.
 * @property extraDisabled Leading and trailing slot color while unavailable.
 * @property extraSelected Leading and trailing slot color while selected.
 * @property bg Container fill at rest.
 * @property bgDisabled Container fill while unavailable.
 * @property bgSelected Container fill while selected.
 * @property shape Shape of the fill and of the press and focus indication.
 */
@Immutable
public data class NavItemStyle(
    public val content: Color,
    public val contentDisabled: Color,
    public val contentSelected: Color,
    public val extra: Color,
    public val extraDisabled: Color,
    public val extraSelected: Color,
    public val bg: Color,
    public val bgDisabled: Color,
    public val bgSelected: Color,
    public val shape: Shape,
) {
    /** The theme's default style, and a builder for overriding it. */
    public companion object {
        /** Theme default: a canvas row whose selection is a muted fill. */
        public val Default: NavItemStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param content Main content color at rest.
         * @param contentDisabled Main content color while unavailable.
         * @param contentSelected Main content color while selected.
         * @param extra Leading and trailing slot color at rest.
         * @param extraDisabled Leading and trailing slot color while unavailable.
         * @param extraSelected Leading and trailing slot color while selected.
         * @param bg Container fill at rest.
         * @param bgDisabled Container fill while unavailable.
         * @param bgSelected Container fill while selected.
         * @param shape Shape of the fill and of the indication.
         * @return Style for a navigation row.
         */
        @Composable
        public fun default(
            content: Color = ArecaTheme.colors.fgBase,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            contentSelected: Color = ArecaTheme.colors.fgBase,
            extra: Color = ArecaTheme.colors.fgMuted,
            extraDisabled: Color = ArecaTheme.colors.fgDisabled,
            extraSelected: Color = ArecaTheme.colors.fgBase,
            bg: Color = ArecaTheme.colors.bgCanvas,
            bgDisabled: Color = ArecaTheme.colors.bgCanvas,
            bgSelected: Color = ArecaTheme.colors.bgMuted,
            shape: Shape = ArecaTheme.shapes.md,
        ): NavItemStyle = NavItemStyle(
            content = content,
            contentDisabled = contentDisabled,
            contentSelected = contentSelected,
            extra = extra,
            extraDisabled = extraDisabled,
            extraSelected = extraSelected,
            bg = bg,
            bgDisabled = bgDisabled,
            bgSelected = bgSelected,
            shape = shape,
        )
    }
}

/**
 * Metrics and typography of a [NavItem].
 *
 * @property icon Size provided to icons in the row through [LocalIconSize].
 * @property chevron Size provided to a trailing chevron through [LocalChevronSize].
 * @property textStyle Style of the main content.
 * @property horizontalPadding Space between the row's edges and its content, before any indent.
 * @property verticalPadding Space above and below the content.
 * @property gap Space between the leading slot, the content and the trailing slot.
 * @property minHeight Smallest height of the row; 48dp by default, the minimum a finger reliably
 *   hits.
 */
@Immutable
public data class NavItemSize(
    public val icon: Dp,
    public val chevron: ChevronSize,
    public val textStyle: TextStyle,
    public val horizontalPadding: Dp,
    public val verticalPadding: Dp,
    public val gap: Dp,
    public val minHeight: Dp = MinimumTouchTarget,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default. */
        public val Default: NavItemSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param icon Size provided to icons in the row.
         * @param chevron Size provided to a trailing chevron.
         * @param textStyle Style of the main content.
         * @param horizontalPadding Space between the row's edges and its content.
         * @param verticalPadding Space above and below the content.
         * @param gap Space between the slots and the content.
         * @param minHeight Smallest height of the row.
         * @return Size for a navigation row.
         */
        @Composable
        public fun default(
            icon: Dp = IconSize.Medium,
            chevron: ChevronSize = ChevronSize.Small,
            textStyle: TextStyle = ArecaTheme.typography.labelMedium,
            horizontalPadding: Dp = ArecaTheme.spacing.lg,
            verticalPadding: Dp = ArecaTheme.spacing.md,
            gap: Dp = ArecaTheme.spacing.md,
            minHeight: Dp = MinimumTouchTarget,
        ): NavItemSize = NavItemSize(
            icon = icon,
            chevron = chevron,
            textStyle = textStyle,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding,
            gap = gap,
            minHeight = minHeight,
        )
    }
}

/** Default [NavItemStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNavItemStyle: ProvidableCompositionLocal<NavItemStyle?> = compositionLocalOf { null }

/** Default [NavItemSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNavItemSize: ProvidableCompositionLocal<NavItemSize?> = compositionLocalOf { null }

/**
 * A single row within a navigation list.
 *
 * Supports optional [leading] and [trailing] slots (icons, badges, chevrons), selected and disabled
 * styling, an optional click, and an [indent] for hierarchy. Colors animate between states on the
 * theme's fast motion step.
 *
 * Assistive technology hears the row as a button, as selected while [selected], and as unavailable
 * while not [enabled] — whether or not it is pressable.
 *
 * @param modifier Modifier applied to the row.
 * @param style Colors and shape. Defaults to the theme's.
 * @param size Metrics and typography. Defaults to the theme's.
 * @param selected Whether the row is the current destination.
 * @param indent Extra start padding, added before [NavItemSize.horizontalPadding], to express
 *   hierarchy.
 * @param enabled Whether the row accepts input and uses its enabled colors.
 * @param interactionSource Source of the row's interactions; pass one to observe them.
 * @param onClick Called when the row is pressed. When non-null, the row shows hover, press and
 *   keyboard focus feedback; when `null`, it is static.
 * @param leading Slot before [content], tinted with the "extra" color.
 * @param trailing Slot after [content], tinted with the "extra" color and given the chevron size.
 * @param content Main row content, typically a [dev.voir.areca.foundation.text.Text].
 */
@Composable
public fun NavItem(
    modifier: Modifier = Modifier,
    style: NavItemStyle = LocalNavItemStyle.current ?: NavItemStyle.Default,
    size: NavItemSize = LocalNavItemSize.current ?: NavItemSize.Default,
    selected: Boolean = false,
    indent: Dp = 0.dp,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val motion = ArecaTheme.motion
    val bgColor by animateColorAsState(
        targetValue = navItemColor(enabled, selected, style.bg, style.bgDisabled, style.bgSelected),
        animationSpec = motion.fast.tween(motion.standard),
        label = "navItemBg",
    )
    val contentColor by animateColorAsState(
        targetValue = navItemColor(
            enabled = enabled,
            selected = selected,
            rest = style.content,
            disabled = style.contentDisabled,
            selectedColor = style.contentSelected,
        ),
        animationSpec = motion.fast.tween(motion.standard),
        label = "navItemContent",
    )
    val extraColor by animateColorAsState(
        targetValue = navItemColor(
            enabled = enabled,
            selected = selected,
            rest = style.extra,
            disabled = style.extraDisabled,
            selectedColor = style.extraSelected,
        ),
        animationSpec = motion.fast.tween(motion.standard),
        label = "navItemExtra",
    )

    val source = interactionSource ?: remember { MutableInteractionSource() }
    val click = onClick

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides size.textStyle,
        LocalIconSize provides size.icon,
    ) {
        Box(
            modifier = modifier
                // A shaped fill, not a clip: clipping would crop the indication's focus ring.
                .background(bgColor, style.shape)
                .then(
                    if (click != null) {
                        Modifier
                            .clickable(
                                enabled = enabled,
                                indication = TouchIndication(
                                    shape = style.shape,
                                    color = ArecaTheme.colors.fgBase,
                                ),
                                interactionSource = source,
                                role = Role.Button,
                                onClick = click,
                            )
                            .clickCursor(enabled)
                    } else {
                        Modifier
                    },
                )
                // Merged so a static row is still read as one item, label and state together.
                .semantics(mergeDescendants = true) {
                    // Only the selected row says so; "not selected" on every other row is noise.
                    if (selected) this.selected = true
                    if (!enabled) disabled()
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = size.minHeight)
                    .padding(
                        start = indent + size.horizontalPadding,
                        end = size.horizontalPadding,
                        top = size.verticalPadding,
                        bottom = size.verticalPadding,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // The content takes the remaining width; the trailing slot is measured first.
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (leading != null) {
                        CompositionLocalProvider(LocalContentColor provides extraColor) {
                            leading()
                            Spacer(modifier = Modifier.width(size.gap))
                        }
                    }
                    content()
                }

                if (trailing != null) {
                    CompositionLocalProvider(
                        LocalContentColor provides extraColor,
                        LocalChevronSize provides size.chevron,
                    ) {
                        Spacer(modifier = Modifier.width(size.gap))
                        trailing()
                    }
                }
            }
        }
    }
}

/**
 * Picks one of a [NavItem] color group's values for a state.
 *
 * @param enabled Whether the row is available. An unavailable row takes [disabled] even while
 *   selected.
 * @param selected Whether the row is the current destination.
 * @param rest Value at rest.
 * @param disabled Value while unavailable.
 * @param selectedColor Value while selected.
 * @return The value for the state.
 */
internal fun navItemColor(
    enabled: Boolean,
    selected: Boolean,
    rest: Color,
    disabled: Color,
    selectedColor: Color,
): Color = when {
    !enabled -> disabled
    selected -> selectedColor
    else -> rest
}
