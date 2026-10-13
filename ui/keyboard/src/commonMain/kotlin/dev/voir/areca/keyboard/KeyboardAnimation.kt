package dev.voir.areca.keyboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlin.time.Duration

/**
 * How a [KeyboardHost]'s keyboard, scrim, and content inset move as the keyboard opens and closes.
 *
 * Three things animate at once and the user reads them as one movement: the keyboard slides up, the
 * scrim fades in, and — when the host compresses its content — the content's bottom inset grows to
 * match the keyboard's height. They are specified together here, and [default] derives all six
 * values from one duration and two easings, because specs that drift apart are exactly what makes a
 * keyboard look like it is coming apart as it opens.
 *
 * @property enter How the keyboard surface arrives.
 * @property exit How the keyboard surface leaves.
 * @property scrimEnter How the scrim arrives.
 * @property scrimExit How the scrim leaves.
 * @property insetEnter How the content's bottom inset grows as the keyboard opens.
 * @property insetExit How the content's bottom inset shrinks as the keyboard closes.
 */
@Immutable
public data class KeyboardAnimation(
    public val enter: EnterTransition,
    public val exit: ExitTransition,
    public val scrimEnter: EnterTransition,
    public val scrimExit: ExitTransition,
    public val insetEnter: FiniteAnimationSpec<Dp>,
    public val insetExit: FiniteAnimationSpec<Dp>,
) {
    /** Theme-backed [KeyboardAnimation] preset and its builder. */
    public companion object {
        /** Theme-backed animation used by [KeyboardHost]. */
        public val Default: KeyboardAnimation
            @Composable
            get() = default()

        /**
         * Builds a coherent set of specs from one duration and a pair of easings.
         *
         * @param duration How long the whole movement takes. The keyboard crosses a large part of
         *   the screen, so this defaults to the theme's `slow` step.
         * @param enterEasing Easing while opening.
         * @param exitEasing Easing while closing.
         * @return An animation whose surface, scrim, and inset all move on the same clock.
         */
        @Composable
        public fun default(
            duration: Duration = ArecaTheme.motion.slow,
            enterEasing: Easing = ArecaTheme.motion.enter,
            exitEasing: Easing = ArecaTheme.motion.exit,
        ): KeyboardAnimation = KeyboardAnimation(
            enter = slideInVertically(duration.tween(enterEasing)) { it } +
                fadeIn(duration.tween(enterEasing)),
            exit = slideOutVertically(duration.tween(exitEasing)) { it } +
                fadeOut(duration.tween(exitEasing)),
            scrimEnter = fadeIn(duration.tween(enterEasing)),
            scrimExit = fadeOut(duration.tween(exitEasing)),
            insetEnter = duration.tween(enterEasing),
            insetExit = duration.tween(exitEasing),
        )
    }
}
