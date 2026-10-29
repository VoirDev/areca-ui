package dev.voir.areca.media.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.media.icon.BundledIcons
import dev.voir.areca.media.icon.ImageX
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * The face a frame draws over content that is not ready: a skeleton, and a spinner once it is slow,
 * while loading; a glyph, a line and a retry when it failed. Nothing when [state] is ready.
 *
 * @param state What the content is doing.
 * @param skeleton Whether loading shows a skeleton; `false` where a stand-in is already drawn.
 * @param onRetry Called to try again, or `null` for a failure that cannot be retried.
 * @param failedText The failed face's line, or `null` for the glyph alone.
 * @param retryLabel The retry button's caption.
 * @param style Colors.
 * @param size Metrics.
 * @param failure Drawn in place of the failed face, handed the retry, or `null` for the face.
 */
@Composable
internal fun MediaFaces(
    state: MediaState,
    skeleton: Boolean,
    onRetry: (() -> Unit)?,
    failedText: String?,
    retryLabel: String,
    style: MediaFrameStyle,
    size: MediaFrameSize,
    failure: (@Composable BoxScope.(retry: () -> Unit) -> Unit)? = null,
) {
    when (state) {
        MediaState.Ready -> Unit
        MediaState.Loading -> LoadingFace(skeleton = skeleton, style = style)
        MediaState.Failed -> if (failure != null) {
            Box(Modifier.fillMaxSize()) { failure(onRetry ?: {}) }
        } else {
            FailedFace(onRetry, failedText, retryLabel, style, size)
        }
    }
}

/**
 * A skeleton the frame's size, and a spinner over it once loading has taken [SLOW_AFTER].
 *
 * @param skeleton Whether to draw the skeleton.
 * @param style Colors.
 */
@Composable
private fun LoadingFace(skeleton: Boolean, style: MediaFrameStyle) {
    var slow by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(SLOW_AFTER)
        slow = true
    }

    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (skeleton) Skeleton(width = maxWidth, height = maxHeight, contentDescription = null)
        if (slow) Spinner(style = SpinnerStyle(color = style.content))
    }
}

/**
 * The glyph for a picture that did not load, with its line and retry where the frame is wide
 * enough; in a narrower frame the glyph alone, the whole face pressable to retry.
 *
 * @param onRetry Called to try again, or `null`.
 * @param failedText The line, or `null`.
 * @param retryLabel The retry button's caption.
 * @param style Colors.
 * @param size Metrics.
 */
@Composable
private fun FailedFace(
    onRetry: (() -> Unit)?,
    failedText: String?,
    retryLabel: String,
    style: MediaFrameStyle,
    size: MediaFrameSize,
) {
    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val compact = maxWidth < size.compactBelow

        Column(
            modifier = Modifier
                .then(
                    if (compact && onRetry != null) {
                        Modifier
                            .fillMaxSize()
                            .clickable(onClickLabel = retryLabel, role = Role.Button, onClick = onRetry)
                    } else {
                        Modifier
                    },
                )
                .semantics(mergeDescendants = true) {}
                .padding(ArecaTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm, Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = BundledIcons.ImageX,
                contentDescription = if (compact) failedText else null,
                tint = style.content,
                size = size.failureGlyph,
            )
            if (!compact && failedText != null) {
                Text(
                    text = failedText,
                    style = ArecaTheme.typography.bodySmall,
                    color = style.content,
                    textAlign = TextAlign.Center,
                )
            }
            if (!compact && onRetry != null) {
                Button(
                    text = retryLabel,
                    onClick = onRetry,
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )
            }
        }
    }
}

/**
 * The gradient of [scrim] over a picture.
 *
 * @param scrim Which edges darken.
 * @param color The darkest step.
 */
@Composable
internal fun ScrimLayer(scrim: MediaScrim, color: Color) {
    val clear = color.copy(alpha = 0f)
    val brush = when (scrim) {
        MediaScrim.None -> return
        MediaScrim.Top -> Brush.verticalGradient(0f to color, SCRIM_REACH to clear)
        MediaScrim.Bottom -> Brush.verticalGradient(1f - SCRIM_REACH to clear, 1f to color)
        MediaScrim.Both -> Brush.verticalGradient(
            0f to color,
            SCRIM_REACH to clear,
            1f - SCRIM_REACH to clear,
            1f to color,
        )
    }
    Box(Modifier.fillMaxSize().background(brush))
}

/** How long a picture loads before its face adds a spinner: past the quick loads that need none. */
private val SLOW_AFTER = 300.milliseconds

/** How far down — or up — a scrim's gradient reaches, as a fraction of the frame. */
private const val SCRIM_REACH = 0.45f

/** The failed face's line when the application gives none. */
internal const val DEFAULT_FAILED_TEXT = "Couldn't load this picture"

/** The retry button's caption when the application gives none. */
internal const val DEFAULT_RETRY_LABEL = "Retry"
