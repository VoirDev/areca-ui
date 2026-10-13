package dev.voir.areca.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import kotlin.time.Duration.Companion.milliseconds

/**
 * Animates a color across the three states a control can rest in: disabled, idle, and active.
 *
 * "Active" is whatever the control's own emphasis means — checked, selected, expanded, focused —
 * and is deliberately not named after any one of them, so a checkbox and a tab can share the timing.
 *
 * The three transitions are timed differently on purpose. Becoming disabled is fastest, because it
 * is a consequence of something else the user did and should not draw the eye. Becoming active is
 * quick and decisive. Returning to idle is slowest, so releasing a control glides rather than snaps.
 *
 * @param enabled Whether the control accepts input. Overrides [active].
 * @param active Whether the control is showing its emphasised state.
 * @param idleColor Color at rest.
 * @param activeColor Color while [active]. Defaults to [idleColor], which animates nothing.
 * @param disabledColor Color while not [enabled]. Defaults to [idleColor].
 * @param label Name for the transition, shown in Compose's animation tooling.
 * @return Animating color, safe to read on every frame.
 */
@Composable
internal fun animateColorFromState(
    enabled: Boolean = true,
    active: Boolean = false,
    idleColor: Color,
    activeColor: Color = idleColor,
    disabledColor: Color = idleColor,
    label: String = "stateColor",
): State<Color> {
    val motion = ArecaTheme.motion
    val target = when {
        !enabled -> ControlColorState.Disabled
        active -> ControlColorState.Active
        else -> ControlColorState.Idle
    }

    val transition = updateTransition(targetState = target, label = label)

    return transition.animateColor(
        transitionSpec = {
            when (targetState) {
                ControlColorState.Disabled -> motion.instant.tween()
                // 120 ms and 220 ms sit between the motion tokens. They are tuned for this
                // transition; move them onto the scale only alongside a visual review.
                ControlColorState.Active -> 120.milliseconds.tween(motion.exit)
                ControlColorState.Idle -> 220.milliseconds.tween(motion.enter)
            }
        },
        label = "$label-value",
    ) { state ->
        when (state) {
            ControlColorState.Disabled -> disabledColor
            ControlColorState.Active -> activeColor
            ControlColorState.Idle -> idleColor
        }
    }
}

/** The three states [animateColorFromState] animates between. */
private enum class ControlColorState {
    /** Control does not accept input. */
    Disabled,

    /** Control accepts input and is not emphasised. */
    Idle,

    /** Control accepts input and is showing its emphasised state. */
    Active,
}
