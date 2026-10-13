package dev.voir.areca.foundation.wheel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import dev.voir.areca.theme.ArecaTheme

/**
 * How a row's appearance falls away from the centre of the wheel.
 *
 * Continuous rather than a selected/unselected pair, which is the whole point: a row halfway between
 * two positions is drawn halfway between two appearances, so the wheel reads as a cylinder turning
 * rather than as a list that highlights a row when the scroll happens to stop.
 *
 * Every rate is *per row of distance* and is applied to a distance first clamped to [maxRows], so
 * the outermost rows stay legible however tall the wheel is.
 *
 * @property scalePerRow Shrink applied per row of distance. `0.08f` means the row two out is drawn
 *   at 84% size.
 * @property alphaPerRow Fade applied per row of distance.
 * @property rotationPerRow Rotation about the horizontal axis per row of distance, in degrees. `0f`
 *   keeps the wheel flat; a value near `18f` with a matching [cameraDistance] gives the drum look of
 *   a physical picker.
 * @property cameraDistance Perspective depth used when [rotationPerRow] is non-zero, in the units of
 *   [androidx.compose.ui.graphics.GraphicsLayerScope.cameraDistance]. Smaller values exaggerate the
 *   curve.
 * @property maxRows Distance at which the falloff stops deepening.
 */
@Immutable
public data class WheelPickerFalloff(
    public val scalePerRow: Float = 0.08f,
    public val alphaPerRow: Float = 0.30f,
    public val rotationPerRow: Float = 0f,
    public val cameraDistance: Float = 12f,
    public val maxRows: Float = 2f,
) {
    init {
        require(maxRows > 0f) { "maxRows must be positive, was $maxRows" }
    }

    /** Named falloffs. */
    public companion object {
        /** Theme default: shrink and fade, no curvature. */
        public val Default: WheelPickerFalloff = WheelPickerFalloff()

        /** Rows tip away from the viewer, as the drum of a physical picker does. */
        public val Curved: WheelPickerFalloff = WheelPickerFalloff(
            scalePerRow = 0.04f,
            alphaPerRow = 0.28f,
            rotationPerRow = 18f,
            maxRows = 3f,
        )
    }
}

/**
 * Colors of a [WheelPicker].
 *
 * The picker draws its own highlight and edge fade from these, because every caller that has ever
 * built a wheel needed both and hand-rolling them is how two wheels in one product end up with
 * different-sized pills. The decoration slots on [WheelPicker] remain for anything genuinely
 * bespoke.
 *
 * [Color.Unspecified] means "draw nothing" rather than "draw transparent", so a picker can opt out
 * of the highlight or the fade without having to supply a colour that happens to be invisible.
 *
 * @property highlight Fill behind the centred row, marking the slot the wheel selects into.
 * @property highlightShape Shape of that fill.
 * @property content Colour of row text at full opacity. [WheelPickerFalloff] fades it with distance.
 * @property surface Colour the top and bottom edges fade to. Must match whatever the picker sits on,
 *   or the fade reads as a band rather than as the wheel curving away.
 * @property selectedFontWeight Weight of the centred row.
 * @property fontWeight Weight of every other row.
 * @property falloff How appearance falls away from the centre.
 */
@Immutable
public data class WheelPickerStyle(
    public val highlight: Color,
    public val highlightShape: Shape,
    public val content: Color,
    public val surface: Color,
    public val selectedFontWeight: FontWeight,
    public val fontWeight: FontWeight,
    public val falloff: WheelPickerFalloff,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: a muted pill behind the centre row, fading to the surface at the edges. */
        public val Default: WheelPickerStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Every parameter defaults to the value [Default] uses, so overriding one stays a one-line
         * change.
         *
         * @param highlight Fill behind the centred row.
         * @param highlightShape Shape of that fill.
         * @param content Colour of row text.
         * @param surface Colour the edges fade to.
         * @param selectedFontWeight Weight of the centred row.
         * @param fontWeight Weight of every other row.
         * @param falloff How appearance falls away from the centre.
         * @return Style built from the given values.
         */
        @Composable
        public fun default(
            highlight: Color = ArecaTheme.colors.bgMuted,
            highlightShape: Shape = ArecaTheme.shapes.md,
            content: Color = ArecaTheme.colors.fgBase,
            surface: Color = ArecaTheme.colors.bgSurface,
            selectedFontWeight: FontWeight = FontWeight.SemiBold,
            fontWeight: FontWeight = FontWeight.Normal,
            falloff: WheelPickerFalloff = WheelPickerFalloff.Default,
        ): WheelPickerStyle = WheelPickerStyle(
            highlight = highlight,
            highlightShape = highlightShape,
            content = content,
            surface = surface,
            selectedFontWeight = selectedFontWeight,
            fontWeight = fontWeight,
            falloff = falloff,
        )
    }
}

/** Default [WheelPickerStyle] in scope, or `null` to use [WheelPickerStyle.Default]. */
public val LocalWheelPickerStyle: ProvidableCompositionLocal<WheelPickerStyle?> =
    compositionLocalOf { null }
