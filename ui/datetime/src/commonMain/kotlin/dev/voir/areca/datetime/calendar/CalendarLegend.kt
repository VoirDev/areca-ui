package dev.voir.areca.datetime.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.foundation.text.Text

/**
 * One entry of a [CalendarLegend]: a dot colour and what it stands for.
 *
 * @property color Colour of the dot, the same one the days' [CalendarMarker.Dots] use.
 * @property label What the colour means: `Income`, `Subscription`.
 */
@Immutable
public data class CalendarLegendItem(
    public val color: Color,
    public val label: String,
)

/**
 * The key to a calendar's [CalendarMarker.Dots]: a centred line of dots with their words, wrapping
 * onto more lines when the width runs out. Place it under the grid, in the order the dots are drawn
 * on the days. Read as one node.
 *
 * @param items Entries in order.
 * @param modifier Modifier applied to the legend, which fills the width it is given.
 * @param contentDescription What assistive technology reads for the whole legend, or `null` for its
 *   words in order.
 * @param style Colours: the words take [CalendarStyle.legendLabel]. Defaults to
 *   [LocalCalendarStyle], or [CalendarStyle.Default].
 * @param size Dot size, type and gaps. Defaults to [LocalCalendarSize], or [CalendarSize.Default].
 */
@Composable
public fun CalendarLegend(
    items: List<CalendarLegendItem>,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
) {
    val description = contentDescription ?: items.joinToString(", ") { it.label }
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(size.legendPadding)
            .clearAndSetSemantics { this.contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(size.legendItemSpacing, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(size.legendItemSpacing / 2),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(size.legendSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.size(size.legendDotSize).background(item.color, CircleShape))
                Text(text = item.label, style = size.legendTextStyle, color = style.legendLabel, maxLines = 1)
            }
        }
    }
}
