package dev.voir.areca.foundation.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors for [Swatch].
 *
 * The border is what makes a swatch readable: a fill alone has no edge against a surface of a
 * similar color, and a color picker is exactly where that happens.
 *
 * @property bg Fill of the swatch — normally the color being shown.
 * @property border Ring around the fill.
 */
@Immutable
public data class SwatchStyle(
    public val bg: Color,
    public val border: Color,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: the accent, ringed in its subtle step. */
        public val Default: SwatchStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the swatch.
         * @param border Ring around the fill.
         * @return Style with the given colors.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.accentBase,
            border: Color = ArecaTheme.colors.accentSubtle,
        ): SwatchStyle = SwatchStyle(bg = bg, border = border)
    }
}

/**
 * Size and border width for [Swatch].
 *
 * @property container Square size of the swatch as drawn.
 * @property borderWidth Width of the ring. Drawn inside [container], so a thicker ring shrinks the
 *   visible fill rather than growing the swatch.
 * @property touchTarget Smallest width and height a pressable swatch occupies in layout. The swatch
 *   is centred in the slot, and the band around it still answers a touch. Every named step reserves
 *   [dev.voir.areca.theme.ArecaSizing.touchTarget]: 48dp, the minimum a finger reliably hits, under
 *   touch, and nothing under a pointer. [Dp.Unspecified] — the constructor's default — reserves
 *   nothing beyond the swatch. A static sample never reserves the band.
 */
@Immutable
public data class SwatchSize(
    public val container: Dp,
    public val borderWidth: Dp,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /** The swatch ramp smallest first, the theme default, and the factory for variations on it. */
    public companion object {
        /** 24dp. A swatch inline with text, or a dense palette grid. */
        public val Small: SwatchSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return SwatchSize(
                    container = 24.dp,
                    borderWidth = 1.5.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 36dp. A color picker's swatch, and the theme default. */
        public val Medium: SwatchSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return SwatchSize(
                    container = 36.dp,
                    borderWidth = 2.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 48dp. A single prominent swatch, such as a currently-selected color. */
        public val Large: SwatchSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return SwatchSize(
                    container = 48.dp,
                    borderWidth = 2.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** Theme default: [Medium]. */
        public val Default: SwatchSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param container Square size of the swatch.
         * @param borderWidth Width of the ring.
         * @param touchTarget Smallest width and height a pressable swatch occupies in layout.
         * @return Size for a swatch.
         */
        @Composable
        public fun default(
            container: Dp = Medium.container,
            borderWidth: Dp = Medium.borderWidth,
            touchTarget: Dp = Medium.touchTarget,
        ): SwatchSize = SwatchSize(
            container = container,
            borderWidth = borderWidth,
            touchTarget = touchTarget,
        )
    }
}

/** Default [SwatchSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSwatchSize: ProvidableCompositionLocal<SwatchSize?> =
    compositionLocalOf { null }

/** Default [SwatchStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSwatchStyle: ProvidableCompositionLocal<SwatchStyle?> =
    compositionLocalOf { null }

/**
 * A single color, shown as a ringed shape.
 *
 * The [content] slot exists so a swatch can show more than a flat fill — a gradient, a transparency
 * checkerboard, a selected-state checkmark — while keeping one ring, one size and one tap target.
 *
 * Pass [onClick] to make it pressable. It then gains hover, press, a keyboard focus ring and a touch
 * band of [SwatchSize.touchTarget]. On its own it announces itself as a button; given [selected] it
 * is one option of a single choice instead, and announces itself as a radio button that is or is not
 * selected — put `Modifier.selectableGroup()` on the container of the group. A swatch conveys its
 * meaning through color alone, so give [contentDescription] the color's name, pressable or not.
 *
 * @param modifier Modifier applied to the swatch.
 * @param style Fill and border colors. Defaults to the theme's.
 * @param size Container size, border width and touch band. Defaults to the theme's, or
 *   [SwatchSize.Default].
 * @param shape Shape of the fill, the border, the indication and the clip on [content] alike.
 * @param selected Whether this swatch is the chosen one of a group, or `null` when it is not part of
 *   a choice. Only read while [onClick] is set.
 * @param contentDescription Name of the color, read by assistive technology as the swatch's
 *   label in both the pressable and the static form. `null` leaves the swatch unnamed.
 * @param enabled Whether a pressable swatch accepts input.
 * @param interactionSource Source of the swatch's interactions; pass one to observe them.
 * @param onClick Called when the swatch is pressed. `null` makes it a static sample.
 * @param content Optional content drawn inside the ring, clipped to [shape].
 */
@Composable
public fun Swatch(
    modifier: Modifier = Modifier,
    style: SwatchStyle = LocalSwatchStyle.current ?: SwatchStyle.Default,
    size: SwatchSize = LocalSwatchSize.current ?: SwatchSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
    selected: Boolean? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val indication = TouchIndication(shape = shape, color = ArecaTheme.colors.fgBase)
    val click = onClick

    Box(
        modifier = modifier
            .thenIf(click != null) { minimumTouchTarget(size.touchTarget) }
            .size(size.container)
            // A shaped fill and border rather than a clip: clipping here would crop the indication's
            // focus ring, which is drawn outside the swatch.
            .border(width = size.borderWidth, color = style.border, shape = shape)
            .background(color = style.bg, shape = shape)
            .then(
                when {
                    click == null -> Modifier
                    selected != null -> Modifier
                        .selectable(
                            selected = selected,
                            interactionSource = source,
                            indication = indication,
                            enabled = enabled,
                            role = Role.RadioButton,
                            onClick = click,
                        )
                        .clickCursor(enabled)

                    else -> Modifier
                        .clickable(
                            enabled = enabled,
                            interactionSource = source,
                            indication = indication,
                            role = Role.Button,
                            onClick = click,
                        )
                        .clickCursor(enabled)
                },
            )
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
            },
    ) {
        if (content != null) {
            // The clip sits on the content alone, for the same reason the container is not clipped.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape),
                contentAlignment = Alignment.Center,
                content = content,
            )
        }
    }
}
