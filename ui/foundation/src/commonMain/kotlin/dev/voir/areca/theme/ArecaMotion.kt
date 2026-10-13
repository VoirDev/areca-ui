package dev.voir.areca.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Animation duration and easing tokens.
 *
 * Durations are grouped by how much of the screen moves, not by which component is moving: a colour
 * swap under a finger is [instant] whether it is a button or a list row, and a sheet crossing the
 * screen is [slow] whether it is a modal or a drawer. Picking by travelled distance is what keeps
 * unrelated components feeling like one product.
 *
 * Build a spec from a token with [tween], so no component spells a raw millisecond count:
 *
 * ```kotlin
 * animateColorAsState(target, animationSpec = ArecaTheme.motion.fast.tween())
 * ```
 */
@Immutable
public data class ArecaMotion(
    /**
     * A state change under the user's finger: press tints, focus rings, checkbox marks.
     *
     * Short enough that the change reads as a direct response rather than an animation.
     */
    public val instant: Duration = 100.milliseconds,
    /** A small element changing in place: a chevron rotating, an indicator sliding between tabs. */
    public val fast: Duration = 150.milliseconds,
    /** The default. A container resizing, a popover appearing, content crossfading. */
    public val medium: Duration = 200.milliseconds,
    /** A surface travelling a large distance: a sheet, a drawer, a full-screen transition. */
    public val slow: Duration = 300.milliseconds,
    /**
     * A deliberately unhurried change the user is meant to watch.
     *
     * Reserve it for one-off moments such as a success confirmation; using it for ordinary
     * interaction makes the product feel sluggish.
     */
    public val deliberate: Duration = 450.milliseconds,
    /** Default easing. Accelerates out of rest and settles into it, for changes in place. */
    public val standard: Easing = FastOutSlowInEasing,
    /** Easing for content arriving on screen: fast in, gentle settle. */
    public val enter: Easing = LinearOutSlowInEasing,
    /** Easing for content leaving the screen: gentle start, quick exit. */
    public val exit: Easing = FastOutLinearInEasing,
    /**
     * Easing for something expanding from the point the user touched: a press radial, a sweep.
     *
     * Leaves rest almost immediately and spends most of its time decelerating, so the expansion
     * reads as a consequence of the touch rather than an animation that happened to start there.
     */
    public val pressIn: Easing = CubicBezierEasing(0.20f, 0.70f, 0.40f, 1f),
    /**
     * Easing for anything fading or crossfading: hover tints, focus rings, background colour swaps.
     *
     * Settles rather than snaps, which is what keeps a released press from reading as a flicker.
     */
    public val fade: Easing = CubicBezierEasing(0.32f, 0.72f, 0.00f, 1f),
)

/**
 * Builds a [TweenSpec] from a motion duration token.
 *
 * @param T Type of the value the spec animates.
 * @param easing Easing curve applied over the duration.
 * @return Spec usable anywhere Compose takes an `animationSpec`.
 */
public fun <T> Duration.tween(easing: Easing = FastOutSlowInEasing): TweenSpec<T> = tween(
    durationMillis = millis,
    easing = easing,
)

/**
 * Millisecond count a Compose animation spec takes.
 *
 * Prefer [tween]. Reach for this only when building a spec that has no duration-typed overload, such
 * as `keyframes` or `repeatable`.
 */
internal val Duration.millis: Int get() = inWholeMilliseconds.toInt()

/** Motion tokens in scope. Read them through [ArecaTheme.motion]. */
internal val LocalMotion = staticCompositionLocalOf { ArecaMotion() }
