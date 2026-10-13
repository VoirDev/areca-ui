package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.attachments.AttachmentFormat
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.flow.attachments.icon.CloudOff
import dev.voir.areca.flow.attachments.icon.DownloadCloud
import dev.voir.areca.flow.attachments.icon.FileText
import dev.voir.areca.flow.attachments.icon.Image
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.Icon

/**
 * The one thing about a file's bytes worth saying on sight, drawn small: arriving or leaving (a
 * ring filling, or a spinner when the share is not known), there to be fetched, or gone.
 *
 * Draws nothing for a file that is here and still — [AttachmentState.Ready] and
 * [AttachmentState.Adding], whose veil says it instead.
 *
 * Silent to assistive technology: the square or the row that carries it says the same as its state
 * description, so a transfer's progress is read once rather than again as a progress indicator.
 *
 * @param state Where the file's bytes are.
 * @param color Colour of the glyph or the ring.
 * @param size Side of the glyph or the ring.
 * @param ringThickness Width of the ring's stroke.
 */
@Composable
internal fun AttachmentStateGlyph(state: AttachmentState, color: Color, size: Dp, ringThickness: Dp) {
    Box(modifier = Modifier.clearAndSetSemantics {}) {
        when (state) {
            is AttachmentState.Downloading -> TransferRing(state.progress, color, size, ringThickness)
            is AttachmentState.Uploading -> TransferRing(state.progress, color, size, ringThickness)
            AttachmentState.Remote, AttachmentState.Waiting -> Icon(
                imageVector = BundledIcons.DownloadCloud,
                contentDescription = null,
                tint = color,
                size = size,
            )

            AttachmentState.Unavailable -> Icon(
                imageVector = BundledIcons.CloudOff,
                contentDescription = null,
                tint = color,
                size = size,
            )

            AttachmentState.Ready, AttachmentState.Adding -> Unit
        }
    }
}

/**
 * How far a transfer has come: a ring that fills clockwise from the top, or a spinner when the
 * share is not known.
 *
 * @param progress Share done, `0..1`, or `null` when not known.
 * @param color Colour of the ring; its track is the same colour, faint.
 * @param size Side of the ring.
 * @param thickness Width of the ring's stroke.
 */
@Composable
private fun TransferRing(progress: Float?, color: Color, size: Dp, thickness: Dp) {
    if (progress == null) {
        Spinner(style = SpinnerStyle(color = color), size = SpinnerSize(container = size, stroke = thickness))
        return
    }

    val sweep = progress.coerceIn(0f, 1f) * FULL_TURN_DEGREES

    Canvas(modifier = Modifier.size(size)) {
        val stroke = thickness.toPx()
        val inset = stroke / 2
        val arcSize = Size(this.size.width - stroke, this.size.height - stroke)

        drawArc(
            color = color.copy(alpha = TRACK_ALPHA),
            startAngle = 0f,
            sweepAngle = FULL_TURN_DEGREES,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = stroke),
        )
        drawArc(
            color = color,
            startAngle = TOP_DEGREES,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

/** @return Whether the state has something for [AttachmentStateGlyph] to draw. */
internal fun AttachmentState.hasGlyph(): Boolean =
    this != AttachmentState.Ready && this != AttachmentState.Adding

/** @return The glyph a file of this format is drawn as when it cannot be drawn itself. */
internal fun AttachmentFormat.icon(): ImageVector = when (this) {
    AttachmentFormat.Image -> BundledIcons.Image
    AttachmentFormat.Pdf, AttachmentFormat.Document -> BundledIcons.FileText
}

/** Opacity of the ring's track, under the filled part. */
private const val TRACK_ALPHA = 0.2f

/** Degrees in a full turn. */
private const val FULL_TURN_DEGREES = 360f

/** Where the ring starts: straight up. */
private const val TOP_DEGREES = -90f
