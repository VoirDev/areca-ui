package dev.voir.areca.foundation.field

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * How a field's state colors move — background, border, value, placeholder, label and ring alike.
 *
 * A state swap under the user's hand: the theme's [dev.voir.areca.theme.ArecaMotion.fast] duration
 * on its fade curve, so focus and validation settle rather than snap and every part of the field
 * arrives at the same moment.
 *
 * @return Spec for every animated field color.
 */
@Composable
internal fun fieldColorSpec(): AnimationSpec<Color> {
    val motion = ArecaTheme.motion
    return motion.fast.tween(motion.fade)
}

/**
 * How a field's placeholder fades as the value empties and fills.
 *
 * The same timing as [fieldColorSpec], so the hint and the colors around it move together.
 *
 * @return Spec for the placeholder's opacity.
 */
@Composable
internal fun fieldFadeSpec(): AnimationSpec<Float> {
    val motion = ArecaTheme.motion
    return motion.fast.tween(motion.fade)
}

/**
 * How a select's chevron turns as its dropdown opens and closes: a small element changing in
 * place, on the theme's [dev.voir.areca.theme.ArecaMotion.fast] duration.
 *
 * @return Spec for the chevron's rotation.
 */
@Composable
internal fun fieldChevronSpec(): AnimationSpec<Float> {
    val motion = ArecaTheme.motion
    return motion.fast.tween(motion.standard)
}

/**
 * How a [FieldGroup]'s message line arrives: a quick fade over a slightly longer expand, so the
 * form makes room before the text is fully there.
 *
 * @return Enter transition for the message.
 */
@Composable
internal fun fieldMessageEnter(): EnterTransition {
    val motion = ArecaTheme.motion
    return fadeIn(motion.fast.tween(motion.enter)) +
        expandVertically(motion.medium.tween<IntSize>(motion.standard))
}

/**
 * How a [FieldGroup]'s message line leaves: the mirror of [fieldMessageEnter].
 *
 * @return Exit transition for the message.
 */
@Composable
internal fun fieldMessageExit(): ExitTransition {
    val motion = ArecaTheme.motion
    return fadeOut(motion.fast.tween(motion.exit)) +
        shrinkVertically(motion.medium.tween<IntSize>(motion.standard))
}
