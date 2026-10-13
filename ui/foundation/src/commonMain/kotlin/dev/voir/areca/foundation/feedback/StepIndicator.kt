package dev.voir.areca.foundation.feedback

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * A row of segments saying how far through a short sequence of steps the user is — the bar at the
 * top of a setup flow.
 *
 * One segment per step. The segments of the steps already done and of the step on screen are
 * filled, so the first step already shows one filled segment: the user is on it, not before it.
 * A segment changes color as the step changes, rather than jumping.
 *
 * Use it for a handful of steps whose count is known. A longer task, or one whose length is
 * unknown, is a [ProgressBar].
 *
 * Assistive technology hears the row as one progress element, at the share of steps reached, named
 * by [contentDescription] — "Step 2 of 3".
 *
 * @param stepCount Number of steps, at least one.
 * @param currentStep Index of the step on screen, coerced into `0 until stepCount`.
 * @param modifier Modifier applied to the row. With [StepIndicatorSize.Fill], give it a width.
 * @param contentDescription What the row says to assistive technology.
 * @param style Colors of the segments.
 * @param size Metrics of the segments.
 * @throws IllegalArgumentException When [stepCount] is less than one.
 */
@Composable
public fun StepIndicator(
    stepCount: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: StepIndicatorStyle = LocalStepIndicatorStyle.current ?: StepIndicatorStyle.Default,
    size: StepIndicatorSize = LocalStepIndicatorSize.current ?: StepIndicatorSize.Compact,
) {
    require(stepCount > 0) { "A step indicator needs at least one step, got $stepCount." }

    val step = currentStep.coerceIn(0, stepCount - 1)
    val reachedShare = (step + 1).toFloat() / stepCount

    Row(
        modifier = modifier.clearAndSetSemantics {
            progressBarRangeInfo = ProgressBarRangeInfo(current = reachedShare, range = 0f..1f)
            if (contentDescription != null) this.contentDescription = contentDescription
        },
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(stepCount) { index ->
            val color by animateColorAsState(
                targetValue = if (index <= step) style.reached else style.ahead,
                animationSpec = ArecaTheme.motion.slow.tween(),
                label = "stepSegmentColor",
            )

            Box(
                Modifier
                    .then(
                        if (size.segmentLength.isSpecified) {
                            Modifier.width(size.segmentLength)
                        } else {
                            Modifier.weight(1f)
                        }
                    )
                    .height(size.thickness)
                    .clip(size.shape)
                    .background(color)
            )
        }
    }
}
