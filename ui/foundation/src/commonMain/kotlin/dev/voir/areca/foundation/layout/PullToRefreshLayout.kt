package dev.voir.areca.foundation.layout

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import dev.voir.areca.ExperimentalArecaApi
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * How far the indicator of a [PullToRefreshLayout] has been pulled.
 *
 * Read-only to callers: the layout drives the state itself, and a caller hoists it only to read
 * [distanceFraction], for example to fade a header as the pull deepens. Create one with
 * [rememberPullToRefreshState].
 */
@ExperimentalArecaApi
@Stable
public sealed interface PullToRefreshState {
    /**
     * Pull distance relative to the refresh threshold.
     *
     * `0f` while the indicator is hidden, `1f` at the threshold, and greater than `1f` while the
     * gesture is pulled beyond it. Never negative.
     */
    @get:FloatRange(from = 0.0)
    public val distanceFraction: Float

    /** Whether the indicator is moving on an animation rather than following a drag. */
    public val isAnimating: Boolean
}

/**
 * Creates and remembers a [PullToRefreshState] with the indicator hidden.
 *
 * @return State remembered for this position in the composition.
 */
@ExperimentalArecaApi
@Composable
public fun rememberPullToRefreshState(): PullToRefreshState = remember { PullToRefreshStateImpl() }

/**
 * Colors of a [PullToRefreshLayout]'s indicator.
 *
 * @property bg Fill of the indicator disc.
 * @property indicator Color of the progress arc and of the spinner shown while refreshing.
 * @property track Ring behind the progress arc, showing how far the pull has left to go.
 */
@ExperimentalArecaApi
@Immutable
public data class PullToRefreshStyle(
    public val bg: Color,
    public val indicator: Color,
    public val track: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: an accent arc on a surface disc. */
        public val Default: PullToRefreshStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the indicator disc.
         * @param indicator Color of the progress arc and the spinner.
         * @param track Ring behind the progress arc.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSurface,
            indicator: Color = ArecaTheme.colors.accentBase,
            track: Color = ArecaTheme.colors.accentMuted,
        ): PullToRefreshStyle = PullToRefreshStyle(bg = bg, indicator = indicator, track = track)
    }
}

/**
 * Metrics of a [PullToRefreshLayout] and its indicator.
 *
 * @property threshold Distance the indicator travels before a release requests a refresh. The
 *   finger travels twice as far, because the pull is damped.
 * @property indicatorSize Diameter of the indicator disc.
 * @property spinner Diameter and stroke of the progress arc and of the spinner inside the disc.
 * @property elevation Shadow under the disc while it is in view.
 */
@ExperimentalArecaApi
@Immutable
public data class PullToRefreshSize(
    public val threshold: Dp,
    public val indicatorSize: Dp,
    public val spinner: SpinnerSize = SpinnerSize.Medium,
    public val elevation: Dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: an 80dp threshold and a 40dp disc. */
        public val Default: PullToRefreshSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param threshold Distance the indicator travels before a release requests a refresh.
         * @param indicatorSize Diameter of the indicator disc.
         * @param spinner Diameter and stroke of the arc inside the disc.
         * @param elevation Shadow under the disc while it is in view.
         * @return The size.
         */
        @Composable
        public fun default(
            threshold: Dp = ArecaTheme.spacing.xl8,
            indicatorSize: Dp = ArecaTheme.spacing.xl5,
            spinner: SpinnerSize = SpinnerSize.Medium,
            elevation: Dp = ArecaTheme.elevation.sm,
        ): PullToRefreshSize = PullToRefreshSize(
            threshold = threshold,
            indicatorSize = indicatorSize,
            spinner = spinner,
            elevation = elevation,
        )
    }
}

/** Default [PullToRefreshStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
@ExperimentalArecaApi
public val LocalPullToRefreshStyle: ProvidableCompositionLocal<PullToRefreshStyle?> =
    compositionLocalOf { null }

/** Default [PullToRefreshSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
@ExperimentalArecaApi
public val LocalPullToRefreshSize: ProvidableCompositionLocal<PullToRefreshSize?> =
    compositionLocalOf { null }

/**
 * Container that adds a pull-to-refresh gesture to scrollable [content].
 *
 * The gesture participates in nested scrolling, so a downward pull is consumed only after the
 * child can no longer scroll toward its beginning. Refreshing is externally controlled: callers
 * should set [isRefreshing] while the work requested by [onRefresh] is in progress.
 *
 * A release past the threshold leaves the indicator resting at the threshold rather than retracting
 * it, so a caller that sets [isRefreshing] in [onRefresh] sees the spinner appear where the pull
 * ended. If [isRefreshing] is still `false` once the indicator settles, it retracts.
 *
 * Anything drawn above the container's top edge is clipped, because the indicator waits there while
 * hidden and would otherwise show over whatever sits above the layout. The other edges are left
 * unclipped.
 *
 * @param isRefreshing Whether refresh work is currently in progress.
 * @param onRefresh Called once when a pull crosses the threshold and is released.
 * @param modifier Modifier applied to the container.
 * @param style Colors of the default indicator. Defaults to the theme's.
 * @param size Threshold and indicator metrics. Defaults to the theme's.
 * @param enabled Whether user pull gestures are accepted.
 * @param state State that tracks the current pull distance.
 * @param contentAlignment Default alignment for children in the container.
 * @param indicator Indicator drawn above [content].
 * @param content Scrollable content, such as a `LazyColumn` or a vertically scrolling layout.
 * @throws IllegalArgumentException When [size]'s threshold is not positive.
 */
@ExperimentalArecaApi
@Composable
public fun PullToRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    style: PullToRefreshStyle = LocalPullToRefreshStyle.current ?: PullToRefreshStyle.Default,
    size: PullToRefreshSize = LocalPullToRefreshSize.current ?: PullToRefreshSize.Default,
    enabled: Boolean = true,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        PullToRefreshIndicator(
            state = state,
            isRefreshing = isRefreshing,
            modifier = Modifier.align(Alignment.TopCenter),
            style = style,
            size = size,
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    require(size.threshold > 0.dp) { "Pull-to-refresh threshold must be greater than zero." }

    val thresholdPx = with(LocalDensity.current) { size.threshold.toPx() }
    val coroutineScope = rememberCoroutineScope()
    val currentOnRefresh = rememberUpdatedState(onRefresh)

    val nestedScrollConnection = remember(state, coroutineScope) {
        PullToRefreshNestedScrollConnection(
            state = state.animatable,
            coroutineScope = coroutineScope,
        )
    }
    nestedScrollConnection.update(
        isRefreshing = isRefreshing,
        enabled = enabled,
        thresholdPx = thresholdPx,
        onRefresh = { currentOnRefresh.value() },
    )

    LaunchedEffect(isRefreshing, enabled, state, thresholdPx) {
        nestedScrollConnection.syncRefreshing(isRefreshing)
    }

    Box(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
            .clipAboveTop(),
        contentAlignment = contentAlignment,
    ) {
        content()
        indicator()
    }
}

/**
 * Default indicator of a [PullToRefreshLayout]: a disc that follows the pull with a progress arc,
 * and shows a spinner while refreshing.
 *
 * The disc waits just above the layout's top edge while hidden and slides down with the pull; it
 * moves without affecting layout, so the content never reflows.
 *
 * @param state Pull distance the disc follows.
 * @param isRefreshing Whether to show the spinner instead of the progress arc.
 * @param modifier Modifier applied to the disc; the layout aligns it top center.
 * @param style Colors of the disc and arc. Defaults to the theme's.
 * @param size Threshold and disc metrics. Defaults to the theme's.
 */
@ExperimentalArecaApi
@Composable
public fun PullToRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    style: PullToRefreshStyle = LocalPullToRefreshStyle.current ?: PullToRefreshStyle.Default,
    size: PullToRefreshSize = LocalPullToRefreshSize.current ?: PullToRefreshSize.Default,
) {
    val density = LocalDensity.current
    val indicatorSizePx = with(density) { size.indicatorSize.roundToPx() }
    val maxDistancePx = with(density) { size.threshold.toPx() }
    val shadowElevation = if (state.distanceFraction > 0f || isRefreshing) {
        size.elevation
    } else {
        ArecaTheme.elevation.none
    }

    Box(
        modifier = modifier
            .size(size.indicatorSize)
            .offsetWithoutAffectingLayout {
                IntOffset(
                    x = 0,
                    y = (state.distanceFraction * maxDistancePx - indicatorSizePx).roundToInt(),
                )
            }
            .shadow(elevation = shadowElevation, shape = CircleShape, clip = true)
            .background(style.bg, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (isRefreshing) {
            Spinner(style = SpinnerStyle(color = style.indicator), size = size.spinner)
        } else {
            PullProgressIndicator(
                progress = state.distanceFraction,
                color = style.indicator,
                trackColor = style.track,
                spinner = size.spinner,
            )
        }
    }
}

/**
 * Clips drawing that extends above the node's top edge, leaving the other three edges unclipped.
 *
 * @return Modifier that clips everything above the node's top edge.
 */
private fun Modifier.clipAboveTop(): Modifier = drawWithContent {
    clipRect(
        left = -Float.MAX_VALUE,
        top = 0f,
        right = Float.MAX_VALUE,
        bottom = Float.MAX_VALUE,
    ) {
        this@drawWithContent.drawContent()
    }
}

/**
 * Places the node at [offset] from where layout put it, without changing its measured size or the
 * space it takes.
 *
 * The offset is read in the placement phase, so a changing pull distance re-places the node without
 * recomposing or remeasuring it.
 *
 * @param offset Offset to place the node at, read on every placement.
 * @return Modifier that offsets the node's placement.
 */
private fun Modifier.offsetWithoutAffectingLayout(
    offset: () -> IntOffset,
): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val resolvedOffset = offset()
            placeable.placeRelativeWithLayer(
                x = resolvedOffset.x,
                y = resolvedOffset.y,
            )
        }
    }
)

/**
 * Arc that grows with the pull, over a full ring showing the distance left.
 *
 * Past the threshold the arc stops growing and rotates instead, so an overpull still reads as
 * movement. Announced as determinate progress while the pull is under way.
 *
 * @param progress Pull distance relative to the threshold; values above `1f` rotate the arc.
 * @param color Color of the arc.
 * @param trackColor Color of the ring behind the arc.
 * @param spinner Diameter and stroke to draw with.
 */
@Composable
private fun PullProgressIndicator(
    @FloatRange(from = 0.0) progress: Float,
    color: Color,
    trackColor: Color,
    spinner: SpinnerSize,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    Canvas(
        modifier = Modifier
            .size(spinner.container)
            .clearAndSetSemantics {
                if (progress > 0f) {
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = coercedProgress,
                        range = 0f..1f,
                    )
                }
            }
    ) {
        val strokeWidth = spinner.stroke.toPx()
        // Stroked on its centre line, so inset by half the stroke to stay in bounds.
        val inset = strokeWidth / 2f
        val sweep = if (coercedProgress == 0f) 0f else 30f + 300f * coercedProgress
        val rotation = -90f + (progress - 1f).coerceAtLeast(0f) * 180f
        val arcSize = Size(
            width = size.width - strokeWidth,
            height = size.height - strokeWidth,
        )

        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth),
        )
        drawArc(
            color = color,
            startAngle = rotation,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
    }
}

/**
 * Nested scroll bridge that turns an overscroll at the top of the content into a pull.
 *
 * Holds the latest composition values through [update] rather than being recreated, so an in-flight
 * gesture keeps its pulled distance across recompositions.
 *
 * @property state State the pull distance is written to.
 * @property coroutineScope Scope of the layout, which runs the indicator's animations.
 */
private class PullToRefreshNestedScrollConnection(
    private val state: PullToRefreshStateImpl,
    private val coroutineScope: CoroutineScope,
) : NestedScrollConnection {
    /** Whether refresh work is in progress, as of the latest composition. */
    private var isRefreshing: Boolean = false

    /** Whether pulls are accepted, as of the latest composition. */
    private var enabled: Boolean = true

    /** Refresh threshold in pixels, as of the latest composition. */
    private var thresholdPx: Float = 1f

    /** Refresh request of the latest composition. */
    private var onRefresh: () -> Unit = {}

    /** Raw finger distance of the current pull, in pixels, before damping. */
    private var distancePulled: Float = 0f

    /**
     * Takes the values of the latest composition.
     *
     * @param isRefreshing Whether refresh work is in progress.
     * @param enabled Whether pulls are accepted.
     * @param thresholdPx Refresh threshold in pixels.
     * @param onRefresh Refresh request to call on a release past the threshold.
     */
    fun update(
        isRefreshing: Boolean,
        enabled: Boolean,
        thresholdPx: Float,
        onRefresh: () -> Unit,
    ) {
        this.isRefreshing = isRefreshing
        this.enabled = enabled
        this.thresholdPx = thresholdPx
        this.onRefresh = onRefresh
    }

    /**
     * Moves the indicator to match a change of the refreshing flag.
     *
     * @param refreshing New value of the flag.
     */
    suspend fun syncRefreshing(refreshing: Boolean) {
        if (refreshing) {
            state.animateToThreshold()
        } else {
            state.animateToHidden()
            distancePulled = 0f
        }
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (
            !enabled ||
            isRefreshing ||
            state.isAnimating ||
            source != NestedScrollSource.UserInput ||
            available.y >= 0f
        ) {
            return Offset.Zero
        }

        return consumeAvailableOffset(available)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (
            !enabled ||
            isRefreshing ||
            state.isAnimating ||
            source != NestedScrollSource.UserInput ||
            available.y <= 0f
        ) {
            return Offset.Zero
        }

        return consumeAvailableOffset(available)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (!enabled || isRefreshing) return Velocity.Zero

        val pulledBeforeRelease = distancePulled
        val releaseFraction = pullReleaseFraction(
            distancePulled = pulledBeforeRelease,
            thresholdPx = thresholdPx,
        )
        if (releaseFraction > 0f) onRefresh()
        distancePulled = 0f

        // Launched rather than awaited: the refreshing effect may take the indicator over, and its
        // animation interrupting this one must not abort the fling that is being dispatched.
        coroutineScope.launch {
            if (releaseFraction > 0f) {
                state.animateToThreshold()
                if (!isRefreshing) state.animateToHidden()
            } else {
                state.animateToHidden()
            }
        }

        val consumedY = consumedPullVelocity(
            distancePulled = pulledBeforeRelease,
            velocityY = available.y,
        )
        return Velocity(x = 0f, y = consumedY)
    }

    /**
     * Adds [available] to the pull and moves the indicator to match.
     *
     * @param available Scroll offered to the pull, in pixels.
     * @return The part of [available] the pull consumed.
     */
    private fun consumeAvailableOffset(available: Offset): Offset {
        val newDistance = (distancePulled + available.y).coerceAtLeast(0f)
        val consumed = newDistance - distancePulled
        distancePulled = newDistance

        val fraction = calculatePullOffset(
            distancePulled = distancePulled,
            thresholdPx = thresholdPx,
        ) / thresholdPx
        coroutineScope.launch {
            if (!state.isAnimating) state.snapTo(fraction)
        }

        return Offset(x = 0f, y = consumed)
    }
}

/**
 * [PullToRefreshState] backed by one animatable fraction, and the only implementation of it.
 *
 * The animations are members of this class rather than of the public interface, so only the layout
 * moves the indicator.
 */
@OptIn(ExperimentalArecaApi::class)
private class PullToRefreshStateImpl : PullToRefreshState {
    /** The animated [distanceFraction]. */
    private val distance = Animatable(0f)

    override val distanceFraction: Float
        get() = distance.value

    override val isAnimating: Boolean
        get() = distance.isRunning

    /** Animates the indicator to rest at the threshold, where it waits while refreshing. */
    suspend fun animateToThreshold() {
        distance.animateTo(1f)
    }

    /** Animates the indicator back out of view. */
    suspend fun animateToHidden() {
        distance.animateTo(0f)
    }

    /**
     * Moves the indicator to [targetValue] without animating.
     *
     * @param targetValue New [distanceFraction].
     * @throws IllegalArgumentException When [targetValue] is negative.
     */
    suspend fun snapTo(@FloatRange(from = 0.0) targetValue: Float) {
        require(targetValue >= 0f) { "Pull-to-refresh distance fraction cannot be negative." }
        distance.snapTo(targetValue)
    }
}

/**
 * The implementation behind a [PullToRefreshState], which the sealed interface guarantees exists.
 */
@OptIn(ExperimentalArecaApi::class)
private val PullToRefreshState.animatable: PullToRefreshStateImpl
    get() = when (this) {
        is PullToRefreshStateImpl -> this
    }

/**
 * Damped distance the indicator travels for a raw finger distance.
 *
 * Linear at half the finger's speed up to the threshold, then easing off so an overpull can reach at
 * most twice the threshold.
 *
 * @param distancePulled Raw finger distance in pixels; negative counts as zero.
 * @param thresholdPx Refresh threshold in pixels.
 * @return Indicator distance in pixels, between `0` and twice [thresholdPx].
 * @throws IllegalArgumentException When [thresholdPx] is not positive.
 */
internal fun calculatePullOffset(distancePulled: Float, thresholdPx: Float): Float {
    require(thresholdPx > 0f) { "Pull-to-refresh threshold must be greater than zero." }

    val adjustedDistance = distancePulled.coerceAtLeast(0f) * DragMultiplier
    if (adjustedDistance <= thresholdPx) return adjustedDistance

    val progress = adjustedDistance / thresholdPx
    val overshootPercent = abs(progress) - 1f
    val linearTension = overshootPercent.coerceIn(0f, 2f)
    val tensionPercent = linearTension - linearTension.pow(2) / 4f
    return thresholdPx + thresholdPx * tensionPercent
}

/**
 * Whether releasing a pull of [distancePulled] requests a refresh.
 *
 * @param distancePulled Raw finger distance in pixels.
 * @param thresholdPx Refresh threshold in pixels.
 * @return `true` when the damped distance reaches the threshold.
 */
internal fun shouldTriggerRefresh(distancePulled: Float, thresholdPx: Float): Boolean =
    distancePulled.coerceAtLeast(0f) * DragMultiplier >= thresholdPx

/**
 * Where the indicator settles when a pull is released.
 *
 * A release that requests a refresh rests at the threshold, where the spinner appears; any other
 * release retracts the indicator.
 *
 * @param distancePulled Raw finger distance in pixels at release.
 * @param thresholdPx Refresh threshold in pixels.
 * @return `1f` to rest at the threshold, `0f` to hide.
 */
internal fun pullReleaseFraction(distancePulled: Float, thresholdPx: Float): Float =
    if (shouldTriggerRefresh(distancePulled, thresholdPx)) 1f else 0f

/**
 * Fling velocity a released pull consumes, so the content does not fling under the indicator.
 *
 * @param distancePulled Raw finger distance in pixels at release.
 * @param velocityY Vertical release velocity; positive is downward.
 * @return The downward velocity while a pull is in progress, otherwise `0`.
 */
internal fun consumedPullVelocity(distancePulled: Float, velocityY: Float): Float = when {
    distancePulled <= 0f -> 0f
    velocityY < 0f -> 0f
    else -> velocityY
}

/** Ratio of indicator travel to finger travel below the threshold. */
private const val DragMultiplier = 0.5f
