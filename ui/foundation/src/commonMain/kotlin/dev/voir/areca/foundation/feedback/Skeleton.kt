package dev.voir.areca.foundation.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/** How the bars of a multi-bar [Skeleton] are laid out. */
public enum class SkeletonArrangement {
    /** Stacked vertically, standing in for lines of wrapped text. */
    Lines,

    /** Side by side horizontally, standing in for a row of chips, tags, or avatars. */
    Row,
}

/**
 * Colors of a [Skeleton]'s bar and its shimmer.
 *
 * The sweep runs from [base] to [highlight], which default to the muted surface and the subtle one.
 * That reads as the placeholder momentarily thinning towards the page — lighter in the light theme,
 * darker in the dark one. Expressing it against the canvas rather than as "lighter" is what keeps it
 * coherent when the theme flips.
 *
 * @property base Resting fill of the bar.
 * @property highlight Color at the centre of the travelling band.
 */
@Immutable
public data class SkeletonStyle(
    public val base: Color,
    public val highlight: Color,
) {
    /** The theme's default [SkeletonStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a muted bar shimmering towards the subtle surface. */
        public val Default: SkeletonStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param base Resting fill of the bar.
         * @param highlight Color at the centre of the travelling band.
         * @return Colors for a skeleton.
         */
        @Composable
        public fun default(
            base: Color = ArecaTheme.colors.bgMuted,
            highlight: Color = ArecaTheme.colors.bgSubtle,
        ): SkeletonStyle = SkeletonStyle(base = base, highlight = highlight)
    }
}

/**
 * Shape, spacing, and shimmer timing for [Skeleton].
 *
 * @property shape Corner shape of each bar.
 * @property lineSpacing Gap between bars, in whichever direction they are arranged.
 * @property shimmerPeriod Time for the band to cross one bar. Slow on purpose: a fast shimmer reads
 *   as an error state rather than as waiting.
 */
@Immutable
public data class SkeletonSize(
    public val shape: Shape,
    public val lineSpacing: Dp,
    public val shimmerPeriod: Duration,
) {
    /** The theme's default [SkeletonSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: SkeletonSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param shape Corner shape of each bar.
         * @param lineSpacing Gap between bars.
         * @param shimmerPeriod Time for the band to cross one bar.
         * @return Metrics for a skeleton.
         */
        @Composable
        public fun default(
            shape: Shape = ArecaTheme.shapes.sm,
            lineSpacing: Dp = ArecaTheme.spacing.sm,
            shimmerPeriod: Duration = 1_500.milliseconds,
        ): SkeletonSize = SkeletonSize(
            shape = shape,
            lineSpacing = lineSpacing,
            shimmerPeriod = shimmerPeriod,
        )
    }
}

/** Default [SkeletonSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSkeletonSize: ProvidableCompositionLocal<SkeletonSize?> =
    compositionLocalOf { null }

/** Default [SkeletonStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSkeletonStyle: ProvidableCompositionLocal<SkeletonStyle?> =
    compositionLocalOf { null }

/**
 * Shimmering bars standing in for content that has not loaded.
 *
 * A skeleton should be the shape of what replaces it. Given no [height], each bar stands in for a
 * line of text in [textStyle]: it reserves that line's height, so nothing jumps when the real text
 * arrives, and draws a bar as tall as the font size centred in it, so the bar reads as a line of
 * text rather than as a box. Give an explicit [width] and [height] for anything that is not text.
 *
 * [count] draws that many bars, arranged by [arrangement]. Vary [width] between sibling skeletons
 * where you can — a block of identical bars reads as a broken layout rather than as loading. Bars
 * with no [width] fill the available width when stacked and share it equally when in a row.
 *
 * Every bar shimmers in step, from one clock read while drawing, so a running shimmer redraws the
 * bars without recomposing them.
 *
 * The whole group is announced as one thing, using [contentDescription], rather than as a run of
 * empty boxes. Pass `null` only when something adjacent already says the screen is loading.
 *
 * @param modifier Modifier applied to the container.
 * @param count Number of bars to draw. Values below one draw a single bar.
 * @param arrangement Whether the bars stack or sit side by side.
 * @param width Width of each bar. `null` fills the available width, shared equally between the bars
 *   of a [SkeletonArrangement.Row].
 * @param height Height of each bar. `null` reserves the line height of [textStyle] and draws the bar
 *   at its font size.
 * @param textStyle Style of the line of text each bar stands in for, which sizes a bar given no
 *   [height]. `null` uses the ambient text style.
 * @param contentDescription What is loading, read by assistive technology.
 * @param animated Whether the shimmer runs. Pass `false` for a static placeholder — in a screenshot
 *   test, or when the user has asked for reduced motion.
 * @param reversed Whether the band travels against the reading direction instead of with it.
 * @param style Bar and shimmer colors. Defaults to the theme's.
 * @param size Shape, spacing, and timing. Defaults to the theme's.
 */
@Composable
public fun Skeleton(
    modifier: Modifier = Modifier,
    count: Int = 1,
    arrangement: SkeletonArrangement = SkeletonArrangement.Lines,
    width: Dp? = null,
    height: Dp? = null,
    textStyle: TextStyle? = null,
    contentDescription: String? = null,
    animated: Boolean = true,
    reversed: Boolean = false,
    style: SkeletonStyle = LocalSkeletonStyle.current ?: SkeletonStyle.Default,
    size: SkeletonSize = LocalSkeletonSize.current ?: SkeletonSize.Default,
) {
    val bars = count.coerceAtLeast(1)
    val line = textStyle ?: LocalTextStyle.current
    val density = LocalDensity.current
    val slotHeight = height ?: with(density) { lineBoxHeight(line.fontSize, line.lineHeight).toDp() }
    val barHeight = height ?: with(density) { line.fontSize.toDp() }

    val shimmer = if (animated) {
        rememberInfiniteTransition(label = "skeletonShimmer").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                // Linear and restarting: a shimmer that eases or bounces reads as a thing moving
                // rather than as a surface being swept.
                animation = tween(durationMillis = size.shimmerPeriod.millis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "skeletonProgress",
        )
    } else {
        null
    }
    val progress: (() -> Float)? = shimmer?.let { state -> { state.value } }

    // The band follows the reading direction, so an RTL locale shimmers right to left unless asked
    // otherwise.
    val towardsEnd = (LocalLayoutDirection.current == LayoutDirection.Ltr) != reversed

    val container = modifier.then(
        if (contentDescription != null) {
            Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
        } else {
            Modifier
        }
    )

    when (arrangement) {
        SkeletonArrangement.Lines -> Column(
            modifier = container,
            verticalArrangement = Arrangement.spacedBy(size.lineSpacing),
        ) {
            repeat(bars) {
                SkeletonBar(
                    modifier = if (width != null) Modifier.width(width) else Modifier.fillMaxWidth(),
                    slotHeight = slotHeight,
                    barHeight = barHeight,
                    style = style,
                    size = size,
                    progress = progress,
                    towardsEnd = towardsEnd,
                )
            }
        }

        SkeletonArrangement.Row -> Row(
            modifier = container,
            horizontalArrangement = Arrangement.spacedBy(size.lineSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(bars) {
                SkeletonBar(
                    modifier = if (width != null) Modifier.width(width) else Modifier.weight(1f),
                    slotHeight = slotHeight,
                    barHeight = barHeight,
                    style = style,
                    size = size,
                    progress = progress,
                    towardsEnd = towardsEnd,
                )
            }
        }
    }
}

/**
 * One bar of a [Skeleton], with the shimmer band travelling across it.
 *
 * The band is drawn as a three-stop gradient whose ends match [SkeletonStyle.base], so it fades into
 * the bar instead of ending at a visible edge. It travels a full band-width beyond each end, so the
 * highlight is fully off the bar at the start and finish of every cycle. [progress] is read only
 * while drawing, so the running shimmer invalidates the bar's drawing and nothing else.
 *
 * @param modifier Modifier carrying the bar's width.
 * @param slotHeight Height the bar reserves in layout.
 * @param barHeight Height of the drawn bar, centred in [slotHeight].
 * @param style Bar and shimmer colors.
 * @param size Shape of the bar.
 * @param progress Where the band is in its cycle, from 0 to 1, or `null` for no band.
 * @param towardsEnd Whether the band travels from the start edge to the end edge.
 */
@Composable
private fun SkeletonBar(
    modifier: Modifier,
    slotHeight: Dp,
    barHeight: Dp,
    style: SkeletonStyle,
    size: SkeletonSize,
    progress: (() -> Float)?,
    towardsEnd: Boolean,
) {
    Spacer(
        modifier = modifier
            .height(slotHeight)
            .drawWithCache {
                val barPx = barHeight.toPx().coerceAtMost(this.size.height)
                val barSize = Size(this.size.width, barPx)
                val top = (this.size.height - barPx) / 2f
                val outline = size.shape.createOutline(barSize, layoutDirection, this)
                val barWidth = barSize.width
                val bandWidth = barWidth * ShimmerBandFraction
                val travel = barWidth + bandWidth * 2f

                onDrawWithContent {
                    translate(top = top) {
                        drawOutline(outline, style.base)
                        if (progress == null) return@translate

                        val phase = progress()
                        val center = if (towardsEnd) {
                            -bandWidth + travel * phase
                        } else {
                            (barWidth + bandWidth) - travel * phase
                        }

                        drawOutline(
                            outline = outline,
                            brush = Brush.linearGradient(
                                colors = listOf(style.base, style.highlight, style.base),
                                start = Offset(center - bandWidth, 0f),
                                end = Offset(center + bandWidth, 0f),
                            ),
                        )
                    }
                }
            }
    )
}

/**
 * Height of the line box a single line of text set at [fontSize] occupies under [lineHeight].
 *
 * A line height in `sp` is the box itself and one in `em` is a multiple of the font size; an
 * unspecified one leaves the box at the font size. The box is never shorter than the font size, so a
 * bar drawn at the font size always fits in it.
 *
 * @param fontSize Font size of the line. Returned unchanged unless it is in `sp`.
 * @param lineHeight Line height of the line.
 * @return The line box height, in `sp` whenever [fontSize] is.
 */
internal fun lineBoxHeight(fontSize: TextUnit, lineHeight: TextUnit): TextUnit {
    if (!fontSize.isSp) return fontSize
    val box = when {
        lineHeight.isSp -> lineHeight.value
        lineHeight.isEm -> fontSize.value * lineHeight.value
        else -> fontSize.value
    }
    return maxOf(box, fontSize.value).sp
}

/** Share of a bar's width the shimmer band covers. */
private const val ShimmerBandFraction = 0.6f

