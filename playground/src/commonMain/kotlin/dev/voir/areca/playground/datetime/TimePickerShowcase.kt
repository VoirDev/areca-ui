package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.TimeFormat
import dev.voir.areca.datetime.TimePicker
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.wheel.WheelPickerFalloff
import dev.voir.areca.foundation.wheel.WheelPickerScroll
import dev.voir.areca.foundation.wheel.WheelPickerSize
import dev.voir.areca.foundation.wheel.WheelPickerStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlinx.datetime.LocalTime

/**
 * A named pace for the playground's picker, since a control needs something it can label.
 *
 * @property label Name shown on the control.
 * @property value Pace to apply, or `null` to let each wheel choose from its own length.
 */
private data class NamedPace(val label: String, val value: WheelPickerScroll?)

/** Each wheel paces itself from how many rows it holds. */
private val AutoPace = NamedPace("Auto", null)

/** The pace a short wheel would choose, applied to every column. */
private val ShortPace = NamedPace("Short", WheelPickerScroll.Short)

/** The pace a long wheel would choose, applied to every column. */
private val LongPace = NamedPace("Long", WheelPickerScroll.Long)

/** Faster than anything chosen automatically. */
private val BriskPace = NamedPace("Brisk", WheelPickerScroll.Brisk)

/** Width the picker is given on this page, so the columns have room without floating apart. */
private val PreviewWidth = 260.dp

/** Showcase for [TimePicker]. */
@Composable
fun TimePickerShowcase() = ShowcaseScaffold(
    title = "Time picker",
    summary = "Hour, minute and an optional AM/PM wheel, reporting the time the wheels come to " +
        "rest on.",
    module = "datetime",
) {
    val noon = remember { LocalTime(12, 30) }

    Playground {
        val twelveHour by booleanControl(
            key = "12-hour",
            default = false,
            description = "TimeFormat.Hours12 adds the AM/PM wheel.",
        )
        val visibleRows by numberControl(
            key = "visibleItemsCount",
            default = 5f,
            range = 3f..9f,
            step = 2f,
            format = { "${it.toInt()}" },
            description = "Rows on screen.",
        )
        val scroll by enumControl(
            key = "scroll",
            default = AutoPace,
            options = listOf(AutoPace, ShortPace, LongPace, BriskPace),
            optionLabel = { pace -> pace.label },
            description = "How hard the wheels answer scrolling. Auto lets each wheel pace itself " +
                "from its own length, so the sixty minutes and the two periods differ.",
        )
        val centerContent by booleanControl(
            key = "centerContent",
            default = true,
            description = "Cluster the columns in the middle, or hug the start edge.",
        )

        preview {
            var time by remember { mutableStateOf(noon) }
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                Box(modifier = Modifier.width(PreviewWidth)) {
                    TimePicker(
                        initialTime = noon,
                        onTimeChange = { time = it },
                        format = if (twelveHour) TimeFormat.Hours12 else TimeFormat.Hours24,
                        size = WheelPickerSize(visibleItemsCount = visibleRows.toInt()),
                        scroll = scroll.value,
                        centerContent = centerContent,
                    )
                }
                Text(
                    text = "Reported: $time",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    Variants(
        title = "Formats",
        description = "The same picker counting hours two ways.",
        layout = VariantLayout.Vertical,
    ) {
        variant("24 hour", detail = "TimeFormat.Hours24") {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(initialTime = noon, onTimeChange = {}, format = TimeFormat.Hours24)
            }
        }
        variant("12 hour", detail = "TimeFormat.Hours12") {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(
                    initialTime = noon,
                    onTimeChange = {},
                    format = TimeFormat.Hours12,
                )
            }
        }
    }

    Variants(
        title = "Falloff",
        description = "The picker takes its wheels' look from a WheelPickerStyle.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Default", detail = "WheelPickerFalloff.Default") {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(initialTime = noon, onTimeChange = {})
            }
        }
        variant("Curved", detail = "WheelPickerFalloff.Curved, 7 rows") {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(
                    initialTime = noon,
                    onTimeChange = {},
                    size = WheelPickerSize(visibleItemsCount = 7),
                    style = WheelPickerStyle.default(falloff = WheelPickerFalloff.Curved),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Scrolling one wheel",
            expectation = "Point at the minutes and scroll: the hours stay where they are, and so " +
                "does this page.",
        ) {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(initialTime = noon, onTimeChange = {})
            }
        }
        case(
            name = "Past midnight",
            expectation = "Hours and minutes repeat, so 23 runs into 00 rather than stopping. " +
                "AM/PM does not.",
        ) {
            Box(modifier = Modifier.width(PreviewWidth)) {
                TimePicker(
                    initialTime = LocalTime(23, 58),
                    onTimeChange = {},
                    format = TimeFormat.Hours12,
                )
            }
        }
        case(
            name = "Narrow container",
            expectation = "Columns keep their width and the picker clips rather than overlapping " +
                "them.",
        ) {
            Box(modifier = Modifier.width(160.dp).fillMaxWidth()) {
                TimePicker(initialTime = noon, onTimeChange = {})
            }
        }
    }
}
