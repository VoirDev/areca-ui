package dev.voir.areca.flow.auth.part

import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

/**
 * Where a mark or a line goes so it lines up with text aligned this way.
 *
 * [TextAlign.Start] and [TextAlign.End] follow the layout direction, so they map to the relative
 * [Alignment.Start] and [Alignment.End]. [TextAlign.Left] and [TextAlign.Right] are absolute, so
 * they map to [AbsoluteAlignment] and stay on the same side in a right-to-left layout.
 *
 * @return The matching horizontal alignment, and centre for every other value.
 */
internal fun TextAlign.toHorizontalAlignment(): Alignment.Horizontal = when (this) {
    TextAlign.Start -> Alignment.Start
    TextAlign.End -> Alignment.End
    TextAlign.Left -> AbsoluteAlignment.Left
    TextAlign.Right -> AbsoluteAlignment.Right
    else -> Alignment.CenterHorizontally
}
