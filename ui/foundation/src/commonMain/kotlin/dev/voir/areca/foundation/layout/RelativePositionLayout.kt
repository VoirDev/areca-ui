package dev.voir.areca.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Where a companion element sits relative to the content it belongs to.
 *
 * The eight entries are the four edges a label can occupy, with the horizontal ones split by
 * alignment. `Start` and `End` place the companion beside the content on the same line; the rest
 * place it on its own line above or below.
 */
public enum class RelativePosition {
    /** Above, aligned to the leading edge. */
    TopStart,

    /** Above, centred. */
    TopCenter,

    /** Above, aligned to the trailing edge. */
    TopEnd,

    /** Below, aligned to the leading edge. */
    BottomStart,

    /** Below, centred. */
    BottomCenter,

    /** Below, aligned to the trailing edge. */
    BottomEnd,

    /** Beside the content, before it in reading order. */
    Start,

    /** Beside the content, after it in reading order. */
    End;

    /** Whether the companion sits on its own line above the content. */
    internal val isAbove: Boolean
        get() = this == TopStart || this == TopCenter || this == TopEnd

    /** Whether the companion sits on its own line below the content. */
    internal val isBelow: Boolean
        get() = this == BottomStart || this == BottomCenter || this == BottomEnd

    /**
     * Cross-axis alignment for a companion on its own line.
     *
     * Meaningless for [Start] and [End], which are placed by the row rather than aligned within it.
     */
    internal val lineAlignment: Alignment
        get() = when (this) {
            TopStart, BottomStart -> Alignment.CenterStart
            TopEnd, BottomEnd -> Alignment.CenterEnd
            else -> Alignment.Center
        }
}

/**
 * Places [overlay] on one of eight sides of [content].
 *
 * Used for a label that belongs to something but is not part of it — a percentage above a progress
 * bar, a caption under a chart. The companion is laid out as a sibling rather than drawn on top, so
 * it takes real space and nothing overlaps.
 *
 * @param position Which side the companion sits on.
 * @param modifier Modifier applied to the whole arrangement.
 * @param space Gap between the companion and the content.
 * @param overlay The companion. `null` renders [content] alone, so a caller with an optional label
 *   does not need a branch.
 * @param content The content the companion belongs to.
 */
@Composable
public fun RelativePositionLayout(
    position: RelativePosition,
    modifier: Modifier = Modifier,
    space: Dp = ArecaTheme.spacing.md,
    overlay: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        if (position.isAbove && overlay != null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = space),
                contentAlignment = position.lineAlignment,
            ) {
                overlay()
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (position == RelativePosition.Start && overlay != null) {
                Box(Modifier.padding(end = space)) { overlay() }
            }

            Box(modifier = Modifier.weight(1f)) { content() }

            if (position == RelativePosition.End && overlay != null) {
                Box(Modifier.padding(start = space)) { overlay() }
            }
        }

        if (position.isBelow && overlay != null) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = space),
                contentAlignment = position.lineAlignment,
            ) {
                overlay()
            }
        }
    }
}
