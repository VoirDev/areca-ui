package dev.voir.areca.playground.datetime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.DateTimeField
import dev.voir.areca.datetime.TimeFormat
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

/** A fixed value, so the page reads the same on every run. */
private val Sample = LocalDateTime(
    date = LocalDate(year = 2026, month = Month.SEPTEMBER, day = 13),
    time = LocalTime(hour = 14, minute = 30),
)

/**
 * Showcase for [DateTimeField]: the composite this module exists to make possible.
 *
 * Two pickers behind one border — the left half opens a calendar, the right half the
 * wheels.
 */
@Composable
fun DateTimeFieldShowcase() = ShowcaseScaffold(
    title = "Date & time field",
    summary = "One field holding a date and a time, each chosen from its own picker.",
    module = "datetime",
) {
    Playground {
        val style by enumControl(
            key = "style",
            default = DateTimeStyleOption.Outlined,
            options = DateTimeStyleOption.entries,
            optionLabel = { it.name },
        )
        val hours by enumControl(
            key = "timeFormat",
            default = TimeFormat.Hours24,
            options = TimeFormat.entries,
            optionLabel = { it.name },
        )
        val status by enumControl(
            key = "status",
            default = FieldStatus.Default,
            options = FieldStatus.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val readOnly by booleanControl("readOnly", default = false)
        val prefilled by booleanControl("prefilled", default = false)

        preview {
            var value by remember(prefilled) {
                mutableStateOf(if (prefilled) Sample else null)
            }

            DateTimeField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                style = style.resolve(),
                status = status,
                enabled = enabled,
                readOnly = readOnly,
                timeFormat = hours,
                timeWidth = 132.dp,
            )
        }
    }

    Variants(
        title = "Styles",
        description = "The composite takes the same presets a single-part field does.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in DateTimeStyleOption.entries) {
            variant(name = option.name) {
                SampleDateTimeField(style = option.resolve(), initial = Sample)
            }
        }
    }

    Sizes(
        description = "Both halves scale together, so the divider stays edge to edge.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Small") { SampleDateTimeField(size = FieldSize.Small, initial = Sample) }
        variant("Medium") { SampleDateTimeField(size = FieldSize.Medium, initial = Sample) }
        variant("Large") { SampleDateTimeField(size = FieldSize.Large, initial = Sample) }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty", detail = "both halves show their placeholder") {
            SampleDateTimeField()
        }
        variant("Chosen") { SampleDateTimeField(initial = Sample) }
        variant("Error", detail = "one status colours the whole row") {
            SampleDateTimeField(initial = Sample, status = FieldStatus.Error)
        }
        variant("Disabled") { SampleDateTimeField(initial = Sample, enabled = false) }
        variant("Read-only", detail = "shows the value, opens neither picker") {
            SampleDateTimeField(initial = Sample, readOnly = true)
        }
    }

    Recipes {
        recipe(
            name = "Scheduling a send",
            description = "Label, hint and validation around the composite, as in a real form.",
        ) {
            var value by remember { mutableStateOf<LocalDateTime?>(null) }

            FieldGroup(
                label = "Send at",
                required = true,
                assistive = "Weekdays only. Times are in your workspace's timezone.",
                error = if (value?.date?.dayOfWeek == DayOfWeek.SATURDAY ||
                    value?.date?.dayOfWeek == DayOfWeek.SUNDAY
                ) {
                    "Pick a weekday."
                } else {
                    null
                },
            ) { field ->
                DateTimeField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.fillMaxWidth(),
                    field = field,
                    timeWidth = 132.dp,
                    datePlaceholder = "Choose a day",
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Rule refusing weekends",
            expectation = "Saturdays and Sundays are drawn but refuse the tap.",
        ) {
            var value by remember { mutableStateOf<LocalDateTime?>(null) }

            DateTimeField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                dateRule = CalendarDateRule { date ->
                    date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY
                },
                timeWidth = 132.dp,
            )
        }

        case(
            name = "Time chosen before a date",
            expectation = "The time half shows the time in twelve-hour form and the date " +
                "half keeps its placeholder; nothing is reported until a date is picked, " +
                "which takes that time.",
        ) {
            var value by remember { mutableStateOf<LocalDateTime?>(null) }

            DateTimeField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                timeFormat = TimeFormat.Hours12,
                timeWidth = 148.dp,
            )
        }
    }
}

/** Style presets the field can be drawn with, as a selectable list. */
private enum class DateTimeStyleOption {
    Outlined,
    Soft,
    Filled,
    Underlined,
}

/**
 * The field with its own state, for sections that vary one thing about it.
 *
 * @param initial Value the field starts with, or null for an empty one.
 * @param style Palette the field is drawn with.
 * @param size Metrics the field is drawn at.
 * @param status Validation outcome the field reports.
 * @param enabled Whether the field opens its pickers.
 * @param readOnly Whether the value can be changed.
 */
@Composable
private fun SampleDateTimeField(
    initial: LocalDateTime? = null,
    style: FieldStyle = FieldStyle.outlined(),
    size: FieldSize = FieldSize.Medium,
    status: FieldStatus = FieldStatus.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    var value by remember { mutableStateOf(initial) }

    DateTimeField(
        value = value,
        onValueChange = { value = it },
        modifier = Modifier.fillMaxWidth(),
        style = style,
        size = size,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        timeWidth = 132.dp,
    )
}

@Composable
private fun DateTimeStyleOption.resolve(): FieldStyle = when (this) {
    DateTimeStyleOption.Outlined -> FieldStyle.outlined()
    DateTimeStyleOption.Soft -> FieldStyle.soft()
    DateTimeStyleOption.Filled -> FieldStyle.filled()
    DateTimeStyleOption.Underlined -> FieldStyle.underlined()
}
