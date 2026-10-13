package dev.voir.areca.foundation.status

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.util.lerp
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import dev.voir.areca.theme.tween
import kotlin.time.Duration

/**
 * A disc that says work is running, and then how it ended.
 *
 * - **Working** — the disc on the page's surface, an arc sweeping round an optional glyph, and
 *   rings breathing outward past its edge. The arc is the [Spinner] every waiting control uses, at
 *   a size a screen can carry; the rings are what keep a long wait from feeling like a frozen frame.
 * - **Success** — the disc lands: it arrives small and settles with a spring, fills with the success
 *   colour, and a tick then draws itself stroke by stroke. A tick that simply exists could have been
 *   there all along; one that writes itself says it has just happened.
 * - **Stopped** — the disc stays as it was, the glyph says what happened, and the rings hold still,
 *   because a halo still breathing would say something is still running.
 *
 * It is one object across the three: pass the new [state] to the same mark and the disc stays where
 * it is while its colour, its contents and its halo change. That continuity is what makes the result
 * read as an answer arriving on this screen rather than as a second screen.
 *
 * The rings are drawn from a clock read while drawing, so a running halo redraws the mark without
 * recomposing it.
 *
 * @param state What the mark shows.
 * @param contentDescription What the mark means, read by assistive technology — "Import complete" —
 *   or `null` when the text beside it already says.
 * @param modifier Modifier applied to the mark.
 * @param style Colors and pace. Defaults to the theme's.
 * @param size Metrics. Defaults to the theme's.
 */
@Composable
public fun StatusMark(
    state: StatusMarkState,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: StatusMarkStyle = LocalStatusMarkStyle.current ?: StatusMarkStyle.Default,
    size: StatusMarkSize = LocalStatusMarkSize.current ?: StatusMarkSize.Default,
) {
    val motion = ArecaTheme.motion
    val succeeded = state is StatusMarkState.Success

    val haloColor by animateColorAsState(
        targetValue = when (state) {
            is StatusMarkState.Working -> style.working
            StatusMarkState.Success -> style.success
            is StatusMarkState.Stopped -> style.stoppedRing
        },
        animationSpec = motion.slow.tween(motion.fade),
        label = "statusMarkHalo",
    )
    val discColor by animateColorAsState(
        targetValue = if (succeeded) style.success else style.disc,
        animationSpec = motion.medium.tween(motion.fade),
        label = "statusMarkDisc",
    )
    val phase = rememberHaloPhase(style.haloPeriod)
    val stopped = state is StatusMarkState.Stopped

    // Starts small when the mark is first composed already successful, so it lands rather than
    // appearing at size and then shrinking to land.
    val scale = remember { Animatable(if (succeeded) DiscEntryScale else 1f) }
    LaunchedEffect(succeeded) {
        if (succeeded) {
            scale.snapTo(DiscEntryScale)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
            )
        }
    }

    Box(
        modifier = modifier
            .size(size.disc * size.haloScale)
            .semantics { if (contentDescription != null) this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawHalo(
                phase = if (stopped) StillHaloPhase else phase.value,
                color = haloColor,
                haloScale = size.haloScale,
            )
        }

        Box(
            modifier = Modifier
                .size(size.disc)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
                .clip(CircleShape)
                .background(discColor),
            contentAlignment = Alignment.Center,
        ) {
            Crossfade(
                targetState = state,
                animationSpec = motion.medium.tween(motion.fade),
                label = "statusMarkContent",
            ) { shown ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    when (shown) {
                        is StatusMarkState.Working -> {
                            Spinner(
                                style = SpinnerStyle(color = style.working),
                                size = SpinnerSize(container = size.spinner),
                            )
                            shown.icon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = style.working,
                                    size = size.icon,
                                )
                            }
                        }

                        StatusMarkState.Success -> Tick(color = style.onSuccess)

                        is StatusMarkState.Stopped -> Icon(
                            imageVector = shown.icon,
                            contentDescription = null,
                            tint = style.stoppedGlyph,
                            size = size.icon,
                        )
                    }
                }
            }
        }
    }
}

/**
 * The tick, writing itself once from the moment it is composed.
 *
 * Held back until the disc has landed, so the two read as one gesture — the mark lands, then it is
 * signed — rather than as two things happening at once.
 *
 * @param color Color of the stroke.
 */
@Composable
private fun Tick(color: Color) {
    val motion = ArecaTheme.motion
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = motion.deliberate.millis,
                delayMillis = motion.medium.millis,
                easing = motion.standard,
            ),
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawTick(progress = progress.value, color = color)
    }
}

/**
 * A value running 0 → 1 over and over, for the halo's rings to ride on.
 *
 * One phase drives every ring, each offset by its share of a turn, so they stay evenly spaced however
 * long the work runs. Linear, because the rings are a pulse, not a bounce.
 *
 * @param period How long one ring takes to cross the halo.
 * @return The phase, to be read while drawing so its every frame does not recompose the mark.
 */
@Composable
private fun rememberHaloPhase(period: Duration): State<Float> {
    val transition = rememberInfiniteTransition(label = "statusMarkHalo")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = period.millis, easing = LinearEasing),
        ),
        label = "statusMarkHaloPhase",
    )
}

/**
 * Rings leaving the disc.
 *
 * Each grows from the disc's own edge outward and fades as it goes, so none ever ends abruptly — a
 * ring that stopped while still visible would read as a glitch rather than as breathing.
 *
 * @param phase Where the first ring is in its journey.
 * @param color Color of the rings.
 * @param haloScale How much wider than the disc the halo is.
 */
private fun DrawScope.drawHalo(phase: Float, color: Color, haloScale: Float) {
    val outer = size.minDimension / 2f
    val inner = outer / haloScale

    repeat(HaloRingCount) { index ->
        val travelled = (phase + index.toFloat() / HaloRingCount) % 1f

        drawCircle(
            color = color,
            radius = lerp(inner, outer, travelled),
            alpha = (1f - travelled) * HaloAlpha,
            style = Stroke(width = size.minDimension * HaloStrokeFraction),
        )
    }
}

/**
 * The tick, drawn as far as it has got.
 *
 * Two strokes measured end to end and revealed by length, not two segments faded in: the pen never
 * lifts, and a short leg finishing with the long one is a tick that was stamped, not written.
 *
 * @param progress How much of the whole tick is drawn, 0 to 1.
 * @param color Color of the stroke.
 */
private fun DrawScope.drawTick(progress: Float, color: Color) {
    if (progress <= 0f) return

    val span = size.minDimension
    val start = Offset(span * 0.30f, span * 0.52f)
    val corner = Offset(span * 0.44f, span * 0.66f)
    val end = Offset(span * 0.72f, span * 0.36f)

    val shortLeg = (corner - start).getDistance()
    val longLeg = (end - corner).getDistance()
    val drawn = (shortLeg + longLeg) * progress
    val width = span * TickStrokeFraction

    drawLine(
        color = color,
        start = start,
        end = lerp(start, corner, (drawn / shortLeg).coerceAtMost(1f)),
        strokeWidth = width,
        cap = StrokeCap.Round,
    )

    if (drawn <= shortLeg) return

    drawLine(
        color = color,
        start = corner,
        end = lerp(corner, end, ((drawn - shortLeg) / longLeg).coerceAtMost(1f)),
        strokeWidth = width,
        cap = StrokeCap.Round,
    )
}

/** Where the rings of a stopped mark are frozen: spread evenly, none of them at an edge. */
private const val StillHaloPhase = 0.25f

/** Rings in flight at once. Three reads as a rhythm; two as a pair, four as noise. */
private const val HaloRingCount = 3

/** Alpha of a ring the instant it leaves the disc, before it starts fading. */
private const val HaloAlpha = 0.34f

/** Ring thickness, as a share of the halo's width, so it scales with the mark. */
private const val HaloStrokeFraction = 0.008f

/** Thickness of the tick, as a share of the disc, so it scales with the mark. */
private const val TickStrokeFraction = 0.08f

/** How small the disc arrives before springing to size on success. */
private const val DiscEntryScale = 0.4f
