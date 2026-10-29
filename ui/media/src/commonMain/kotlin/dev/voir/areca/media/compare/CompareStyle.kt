package dev.voir.areca.media.compare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/** How two compared pictures are placed. */
public enum class CompareArrangement {
    /** One above the other — a phone held upright. */
    Column,

    /** Side by side. */
    Row,
}

/**
 * Words over two compared pictures.
 *
 * @property first Over the first picture — "Before", "Your photo".
 * @property second Over the second — "After", "In our catalogue".
 */
@Immutable
public data class CompareLabels(
    public val first: String = "Before",
    public val second: String = "After",
)

/**
 * Colors and shape of the compare parts.
 *
 * @property shape Shape of each picture's frame.
 * @property bg Fill behind a picture that does not cover its frame.
 * @property divider The divider line and the fade track.
 * @property knob The knob's fill.
 * @property knobContent The knob's glyph.
 * @property labelBg Fill behind a label on a picture.
 * @property labelContent Words of a label on a picture.
 */
@Immutable
public data class CompareStyle(
    public val shape: Shape,
    public val bg: Color,
    public val divider: Color,
    public val knob: Color,
    public val knobContent: Color,
    public val labelBg: Color,
    public val labelContent: Color,
) {
    /** The theme's compare parts. */
    public companion object {
        /**
         * White divider and knob, readable on any photograph, with labels on dark capsules.
         *
         * @param shape Shape of each picture's frame.
         * @param bg Fill behind a picture.
         * @return The style.
         */
        @Composable
        public fun default(
            shape: Shape = ArecaTheme.shapes.lg,
            bg: Color = ArecaTheme.colors.bgSubtle,
        ): CompareStyle = CompareStyle(
            shape = shape,
            bg = bg,
            divider = ArecaTheme.baseColors.baseWhite,
            knob = ArecaTheme.baseColors.baseWhite,
            knobContent = ArecaTheme.baseColors.baseBlack,
            labelBg = ArecaTheme.baseColors.baseBlack.copy(alpha = LABEL_ALPHA),
            labelContent = ArecaTheme.baseColors.baseWhite,
        )
    }
}

/** How dark a label's capsule is over a picture. */
private const val LABEL_ALPHA = 0.6f

/**
 * A word over a compared picture — "Before", "Your photo" — as a badge in the style's label colours.
 *
 * @param text The word.
 * @param modifier Modifier applied to the badge; align it in the frame.
 * @param style The compare style, for its label colours.
 */
@Composable
internal fun CompareLabel(text: String, modifier: Modifier, style: CompareStyle) {
    Badge(
        modifier = modifier,
        style = BadgeStyle(bg = style.labelBg, content = style.labelContent, border = Color.Transparent),
        size = BadgeSize.Small,
    ) { Text(text) }
}
