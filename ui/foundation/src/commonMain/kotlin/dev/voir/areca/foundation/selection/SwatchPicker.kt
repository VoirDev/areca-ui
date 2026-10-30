package dev.voir.areca.foundation.selection

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Check
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * A named group of related colors, shown as one column by [SwatchPickerLayout.GroupedColumns].
 *
 * @property id Identity of the group. Must be unique among the groups passed to one [SwatchPicker];
 *   it keys the group's column.
 * @property name Display name of the group. May repeat; it is never used as identity.
 * @property items Colors in the group, in display order.
 */
public data class ColorShade(
    public val id: String,
    public val name: String,
    public val items: List<ColorItem>,
)

/**
 * One pickable color.
 *
 * @property id Identity of the color. Must be unique across every group passed to one
 *   [SwatchPicker]; it keys the swatch and decides which swatch is selected.
 * @property name Display name of the color, read by assistive technology as the swatch's label. May
 *   repeat; it is never used as identity.
 * @property border Ring drawn around the swatch.
 * @property bg Fill of the swatch.
 */
public data class ColorItem(
    public val id: String,
    public val name: String,
    public val border: Color,
    public val bg: Color,
)

/**
 * Whether [color] is the picker's current selection.
 *
 * Matches by [ColorItem.id], never by [ColorItem.name], so two colors that share a name are not
 * both shown as selected.
 *
 * @param color Swatch being drawn.
 * @param selected Picker's current selection, or `null` when nothing is selected.
 * @return `true` when [color] and [selected] are the same color.
 */
internal fun isSwatchSelected(color: ColorItem, selected: ColorItem?): Boolean =
    color.id == selected?.id

/** How a [SwatchPicker] arranges its groups. */
public enum class SwatchPickerLayout {
    /** One column per [ColorShade], the columns scrolling sideways. */
    GroupedColumns,

    /** Every color of every group in one scrolling row, in group order. */
    FlatRow,
}

/**
 * Colors of a [SwatchPicker] that do not come from the colors it offers.
 *
 * The check on the selected swatch sits on that swatch's own color, which can be anything from
 * white to black. One fixed check color disappears on half of any real palette, so the picker
 * carries two and draws whichever contrasts more with the swatch underneath.
 *
 * @property checkOnDark Check drawn on a dark swatch.
 * @property checkOnLight Check drawn on a light swatch.
 */
@Immutable
public data class SwatchPickerStyle(
    public val checkOnDark: Color,
    public val checkOnLight: Color,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: a white check on dark swatches, a near-black one on light swatches. */
        public val Default: SwatchPickerStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Both defaults are palette endpoints rather than semantic foregrounds: the check sits on
         * the offered color, not on the theme's surface, so it must not flip with the theme.
         *
         * @param checkOnDark Check drawn on a dark swatch.
         * @param checkOnLight Check drawn on a light swatch.
         * @return Style for a picker.
         */
        @Composable
        public fun default(
            checkOnDark: Color = ArecaTheme.baseColors.baseWhite,
            checkOnLight: Color = ArecaTheme.baseColors.neutral100,
        ): SwatchPickerStyle = SwatchPickerStyle(
            checkOnDark = checkOnDark,
            checkOnLight = checkOnLight,
        )
    }
}

/**
 * Metrics of a [SwatchPicker].
 *
 * The selected swatch can differ from the rest in size, ring and scale. Size and ring change the
 * layout; scale only changes the drawing, so a selected swatch that grows by [swatchScaleSelected]
 * never pushes its neighbours.
 *
 * @property spacing Gap between neighbouring swatches, and between groups.
 * @property swatch Size of an unselected swatch.
 * @property swatchBorder Ring width of an unselected swatch.
 * @property swatchScale Drawing scale of an unselected swatch.
 * @property swatchSelected Size of the selected swatch.
 * @property swatchBorderSelected Ring width of the selected swatch.
 * @property swatchScaleSelected Drawing scale of the selected swatch.
 * @property icon Size of the check on the selected swatch.
 * @property touchTarget Smallest width and height each swatch occupies in layout; see
 *   [SwatchSize.touchTarget].
 */
@Immutable
public data class SwatchPickerSize(
    public val spacing: Dp,
    public val swatch: Dp,
    public val swatchBorder: Dp,
    public val swatchScale: Float = 1f,
    public val swatchSelected: Dp = swatch,
    public val swatchBorderSelected: Dp = swatchBorder,
    public val swatchScaleSelected: Float = 1.1f,
    public val icon: Dp = swatch / 2,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: 36dp swatches, the selected one drawn a tenth larger. */
        public val Default: SwatchPickerSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param spacing Gap between neighbouring swatches, and between groups.
         * @param swatch Size of an unselected swatch.
         * @param swatchBorder Ring width of an unselected swatch.
         * @param swatchScale Drawing scale of an unselected swatch.
         * @param swatchSelected Size of the selected swatch.
         * @param swatchBorderSelected Ring width of the selected swatch.
         * @param swatchScaleSelected Drawing scale of the selected swatch.
         * @param icon Size of the check on the selected swatch.
         * @param touchTarget Smallest width and height each swatch occupies in layout.
         * @return Size for a picker.
         */
        @Composable
        public fun default(
            spacing: Dp = ArecaTheme.spacing.md,
            swatch: Dp = SwatchSize.Medium.container,
            swatchBorder: Dp = SwatchSize.Medium.borderWidth,
            swatchScale: Float = 1f,
            swatchSelected: Dp = swatch,
            swatchBorderSelected: Dp = swatchBorder,
            swatchScaleSelected: Float = 1.1f,
            icon: Dp = swatch / 2,
            touchTarget: Dp = ArecaTheme.sizing.touchTarget,
        ): SwatchPickerSize = SwatchPickerSize(
            spacing = spacing,
            swatch = swatch,
            swatchBorder = swatchBorder,
            swatchScale = swatchScale,
            swatchSelected = swatchSelected,
            swatchBorderSelected = swatchBorderSelected,
            swatchScaleSelected = swatchScaleSelected,
            icon = icon,
            touchTarget = touchTarget,
        )
    }
}

/** Default [SwatchPickerStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSwatchPickerStyle: ProvidableCompositionLocal<SwatchPickerStyle?> =
    compositionLocalOf { null }

/** Default [SwatchPickerSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSwatchPickerSize: ProvidableCompositionLocal<SwatchPickerSize?> =
    compositionLocalOf { null }

/**
 * A scrollable set of color swatches with a single selection.
 *
 * Swatches are keyed and matched by [ColorItem.id], and groups by [ColorShade.id]. Ids must be
 * unique; names are display-only and may repeat.
 *
 * The picker is one selectable group: each swatch is announced by its [ColorItem.name] as a radio
 * button that is or is not selected, and the check on the selected swatch takes whichever of
 * [SwatchPickerStyle]'s two colors reads on it.
 *
 * @param items Groups of colors to offer.
 * @param selected Currently selected color, matched by [ColorItem.id]. `null` selects nothing.
 * @param onChange Called with the color the user picks.
 * @param modifier Modifier applied to the row.
 * @param style Check colors. Defaults to the theme's.
 * @param size Swatch, ring, scale, check and spacing sizes. Defaults to the theme's.
 * @param layout Whether groups are shown as columns or flattened into one row.
 * @param contentPadding Padding around the scrolled content.
 * @param enabled Whether the swatches accept input.
 */
@Composable
public fun SwatchPicker(
    items: List<ColorShade>,
    selected: ColorItem?,
    onChange: (color: ColorItem) -> Unit,
    modifier: Modifier = Modifier,
    style: SwatchPickerStyle = LocalSwatchPickerStyle.current ?: SwatchPickerStyle.Default,
    size: SwatchPickerSize = LocalSwatchPickerSize.current ?: SwatchPickerSize.Default,
    layout: SwatchPickerLayout = SwatchPickerLayout.GroupedColumns,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    enabled: Boolean = true,
) {
    val rowModifier = modifier
        .fillMaxWidth()
        .selectableGroup()

    when (layout) {
        SwatchPickerLayout.GroupedColumns -> LazyRow(
            modifier = rowModifier,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(size.spacing),
        ) {
            items(items = items, key = { it.id }) { shade ->
                Column(verticalArrangement = Arrangement.spacedBy(size.spacing)) {
                    shade.items.forEach { color ->
                        SwatchItem(
                            color = color,
                            selected = isSwatchSelected(color, selected),
                            style = style,
                            size = size,
                            enabled = enabled,
                            onClick = { onChange(color) },
                        )
                    }
                }
            }
        }

        SwatchPickerLayout.FlatRow -> {
            val flat = remember(items) { items.flatMap { it.items } }

            LazyRow(
                modifier = rowModifier,
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(size.spacing),
            ) {
                items(items = flat, key = { it.id }) { color ->
                    SwatchItem(
                        color = color,
                        selected = isSwatchSelected(color, selected),
                        style = style,
                        size = size,
                        enabled = enabled,
                        onClick = { onChange(color) },
                    )
                }
            }
        }
    }
}

/**
 * One swatch of a [SwatchPicker], animating between its unselected and selected metrics.
 *
 * @param color Color the swatch offers.
 * @param selected Whether it is the picker's selection.
 * @param style Check colors.
 * @param size Picker metrics.
 * @param enabled Whether the swatch accepts input.
 * @param onClick Called when the swatch is picked.
 */
@Composable
private fun SwatchItem(
    color: ColorItem,
    selected: Boolean,
    style: SwatchPickerStyle,
    size: SwatchPickerSize,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val motion = ArecaTheme.motion
    val container by animateDpAsState(
        targetValue = if (selected) size.swatchSelected else size.swatch,
        animationSpec = motion.slow.tween(motion.standard),
        label = "swatchPickerContainer",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (selected) size.swatchBorderSelected else size.swatchBorder,
        animationSpec = motion.slow.tween(motion.standard),
        label = "swatchPickerBorder",
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) size.swatchScaleSelected else size.swatchScale,
        animationSpec = motion.slow.tween(motion.standard),
        label = "swatchPickerScale",
    )

    Swatch(
        modifier = Modifier.scale(scale),
        style = SwatchStyle(bg = color.bg, border = color.border),
        size = SwatchSize(
            container = container,
            borderWidth = borderWidth,
            touchTarget = size.touchTarget,
        ),
        selected = selected,
        contentDescription = color.name,
        enabled = enabled,
        onClick = onClick,
    ) {
        if (selected) {
            Icon(
                imageVector = BundledIcons.Check,
                contentDescription = null,
                tint = swatchCheckColor(
                    background = color.bg,
                    onDark = style.checkOnDark,
                    onLight = style.checkOnLight,
                ),
                modifier = Modifier.size(size.icon),
            )
        }
    }
}

/**
 * The check color that reads on a swatch of [background].
 *
 * Picks by WCAG contrast rather than by a lightness threshold, so a mid-tone swatch gets whichever
 * check actually stands out on it. A translucent swatch is judged as drawn over nothing, which is
 * what its own fill alone looks like.
 *
 * @param background Fill of the swatch.
 * @param onDark Candidate for a dark swatch.
 * @param onLight Candidate for a light swatch.
 * @return [onDark] or [onLight], whichever contrasts more with [background]; [onDark] on a tie.
 */
internal fun swatchCheckColor(background: Color, onDark: Color, onLight: Color): Color =
    if (contrastRatio(onLight, background) > contrastRatio(onDark, background)) onLight else onDark

/**
 * WCAG 2.1 contrast ratio between two colors.
 *
 * @param a One color.
 * @param b The other.
 * @return The ratio, from 1 (identical luminance) to 21 (black on white).
 */
internal fun contrastRatio(a: Color, b: Color): Float {
    val lighter = maxOf(a.luminance(), b.luminance())
    val darker = minOf(a.luminance(), b.luminance())
    return (lighter + ContrastFlare) / (darker + ContrastFlare)
}

/** Flare term WCAG adds to both luminances so a ratio against black stays finite. */
private const val ContrastFlare = 0.05f
