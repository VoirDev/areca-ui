package dev.voir.areca.foundation.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.LayoutDirection

/**
 * One shimmering shape standing in for something that is not text: an avatar, a thumbnail, a chip,
 * a button, a card.
 *
 * Unlike [Skeleton], which sizes its bars from a line of text, a block is exactly the size its
 * [modifier] gives it — `Modifier.size(48.dp)`, `Modifier.fillMaxWidth().aspectRatio(1f)`,
 * `Modifier.fillMaxWidth().height(ArecaTheme.sizing.controlMd)` — so it can match what replaces it.
 * It is silent to assistive technology; wrap a region of placeholders in [SkeletonGroup] to say
 * once what is loading.
 *
 * @param modifier Modifier that sizes the block.
 * @param shape Shape of the block: `ArecaTheme.shapes.full` for an avatar or a chip,
 *   `shapes.surface` for a card. Defaults to the skeleton size's shape.
 * @param animated Whether the shimmer runs.
 * @param reversed Whether the band travels against the reading direction instead of with it.
 * @param style Block and shimmer colors. Defaults to the theme's.
 * @param size Timing, and the default [shape]. Defaults to the theme's.
 */
@Composable
public fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    animated: Boolean = true,
    reversed: Boolean = false,
    style: SkeletonStyle = LocalSkeletonStyle.current ?: SkeletonStyle.Default,
    size: SkeletonSize = LocalSkeletonSize.current ?: SkeletonSize.Default,
) {
    val outlineShape = shape ?: size.shape
    val progress: (() -> Float)? = if (animated) {
        val transition = rememberInfiniteTransition(label = "skeletonBlockShimmer")
        val state = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = size.shimmerPeriod.inWholeMilliseconds.toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "skeletonBlockProgress",
        )
        ({ state.value })
    } else {
        null
    }
    val towardsEnd = (LocalLayoutDirection.current == LayoutDirection.Ltr) != reversed

    Spacer(
        modifier = modifier.drawWithCache {
            val outline = outlineShape.createOutline(this.size, layoutDirection, this)
            val width = this.size.width
            val band = width * BlockBandFraction
            val travel = width + band * 2f
            onDrawWithContent {
                drawOutline(outline, style.base)
                val phase = progress?.invoke() ?: return@onDrawWithContent
                val center = if (towardsEnd) -band + travel * phase else (width + band) - travel * phase
                drawOutline(
                    outline = outline,
                    brush = Brush.linearGradient(
                        colors = listOf(style.base, style.highlight, style.base),
                        start = Offset(center - band, 0f),
                        end = Offset(center + band, 0f),
                    ),
                )
            }
        },
    )
}

/**
 * A region of placeholders announced once: [contentDescription] is read for the whole region, and
 * the skeletons inside are silent — rather than a run of empty boxes each saying "loading".
 *
 * @param contentDescription What is loading — "Loading transactions".
 * @param modifier Modifier applied to the region.
 * @param content The placeholders, laid out in a column.
 */
@Composable
public fun SkeletonGroup(
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.clearAndSetSemantics { this.contentDescription = contentDescription },
        content = content,
    )
}

/** Width of the shimmer band as a share of the block's width. */
private const val BlockBandFraction = 0.6f
