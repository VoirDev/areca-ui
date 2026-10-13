package dev.voir.areca.foundation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.list.LocalListItemLayout
import dev.voir.areca.foundation.slot.SlotRowLayout

/**
 * The slot layout a settings row uses by default, aligned for how much text it carries.
 *
 * A row with only a title keeps the list's layout, which centres the glyph, the title and the
 * control on one line. A row with a subtitle is aligned to the top instead: centred against two or
 * three lines of text, the glyph and the trailing content float halfway down a paragraph and stop
 * pointing at the title they belong to.
 *
 * Only the alignments change — widths, gaps and arrangement stay whatever the list set.
 *
 * @param hasSubtitle Whether the row draws a subtitle under its title.
 * @return The list's layout, top-aligned when [hasSubtitle].
 */
@Composable
internal fun settingsRowLayout(hasSubtitle: Boolean): SlotRowLayout {
    val layout = LocalListItemLayout.current
    if (!hasSubtitle) return layout

    return layout.copy(
        startAlignment = Alignment.TopStart,
        centerAlignment = Alignment.TopStart,
        endAlignment = Alignment.TopEnd,
        verticalAlignment = Alignment.Top,
    )
}
