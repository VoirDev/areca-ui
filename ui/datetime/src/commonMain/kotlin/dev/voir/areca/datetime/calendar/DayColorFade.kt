package dev.voir.areca.datetime.calendar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

/**
 * Carries a color as premultiplied alpha, so a fade to or from nothing stays its own color.
 *
 * [Color.Transparent] is transparent **black**, and the ordinary color animation moves every channel
 * at once: fading a day's fill in from it climbs out of black while it becomes opaque, and halfway
 * there a near-white lavender is a dark grey at half opacity. Over a light surface that composites
 * to grey — which is the flash a calendar shows on every cell whose fill appears or disappears.
 *
 * Premultiplied, transparent-black and transparent-anything are the same point, so the path from
 * nothing to a color is that color at rising alpha and never a slide through another one.
 *
 * The vector is `(alpha, red·alpha, green·alpha, blue·alpha)`, converted through
 * [ColorSpaces.Srgb] so the multiplication means the same thing whatever space the color arrived in.
 */
internal val DayColorConverter: TwoWayConverter<Color, AnimationVector4D> =
    TwoWayConverter(
        convertToVector = { color ->
            val srgb = color.convert(ColorSpaces.Srgb)
            AnimationVector4D(
                srgb.alpha,
                srgb.red * srgb.alpha,
                srgb.green * srgb.alpha,
                srgb.blue * srgb.alpha,
            )
        },
        convertFromVector = { vector ->
            val alpha = vector.v1.coerceIn(0f, 1f)
            // Nothing is nothing: at zero alpha the channels carry no recoverable color, and
            // dividing by it would be a division by zero rather than a hue.
            if (alpha == 0f) {
                Color.Transparent
            } else {
                Color(
                    red = (vector.v2 / alpha).coerceIn(0f, 1f),
                    green = (vector.v3 / alpha).coerceIn(0f, 1f),
                    blue = (vector.v4 / alpha).coerceIn(0f, 1f),
                    alpha = alpha,
                )
            }
        },
    )

/**
 * Animates one of a day cell's colors.
 *
 * Use this rather than [androidx.compose.animation.animateColorAsState] anywhere a cell's color can
 * be [Color.Transparent] at either end — which is every one of them: a fill appears when a day is
 * chosen, a band appears when a range reaches it, and today's ring appears on one cell out of
 * forty-two. See [DayColorConverter] for what the ordinary animation does to those.
 *
 * @param targetValue Color to settle on.
 * @param animationSpec How the color travels there.
 * @param label Name for animation inspection tools.
 * @return The color as it currently stands, updating each frame.
 */
@Composable
internal fun animateDayColorAsState(
    targetValue: Color,
    animationSpec: AnimationSpec<Color>,
    label: String,
): State<Color> = animateValueAsState(
    targetValue = targetValue,
    typeConverter = DayColorConverter,
    animationSpec = animationSpec,
    label = label,
)

