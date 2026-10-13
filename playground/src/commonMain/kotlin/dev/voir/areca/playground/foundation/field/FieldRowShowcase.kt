package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.FieldLabelPlacement
import dev.voir.areca.foundation.field.FieldRow
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.TextSegment
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.field.select.SelectSegment
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.Ruler
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Currencies offered beside an amount. */
private val Currencies = listOf(
    SelectOption(key = "usd", value = "USD", label = "USD"),
    SelectOption(key = "eur", value = "EUR", label = "EUR"),
    SelectOption(key = "gbp", value = "GBP", label = "GBP"),
    SelectOption(key = "jpy", value = "JPY", label = "JPY"),
)

/** Dialling codes offered before a phone number. */
private val DiallingCodes = listOf(
    SelectOption(key = "us", value = "+1", label = "+1"),
    SelectOption(key = "uk", value = "+44", label = "+44"),
    SelectOption(key = "de", value = "+49", label = "+49"),
    SelectOption(key = "ua", value = "+380", label = "+380"),
)

/** Units offered beside a measurement. */
private val Units = listOf(
    SelectOption(key = "cm", value = "cm", label = "cm"),
    SelectOption(key = "in", value = "in", label = "in"),
    SelectOption(key = "mm", value = "mm", label = "mm"),
)

/**
 * Showcase for [FieldRow]: several controls sharing one field.
 *
 * The page a reader lands on to answer one question — how do I put two things in one box without
 * it looking like two boxes — so every sample is a pairing somebody actually ships.
 */
@Composable
fun FieldRowShowcase() = ShowcaseScaffold(
    title = "Field row",
    summary = "Several controls inside one field, sharing its border, focus and status.",
    module = "foundation",
) {
    Playground {
        val style by enumControl(
            key = "style",
            default = CompositeStyleOption.Outlined,
            options = CompositeStyleOption.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = CompositeSizeOption.Medium,
            options = CompositeSizeOption.entries,
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
        val divided by booleanControl("divider", default = true)

        preview {
            var amount by remember { mutableStateOf("") }
            var currency by remember { mutableStateOf(setOf("USD")) }

            FieldRow(
                modifier = Modifier.fillMaxWidth(),
                style = style.resolve(),
                size = size.resolve(),
                status = status,
                enabled = enabled,
                readOnly = readOnly,
            ) {
                TextSegment(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text("0.00") },
                )
                if (divided) SegmentDivider()
                SelectSegment(
                    values = currency,
                    onChange = { currency = it },
                    options = Currencies,
                    width = 96.dp,
                )
            }
        }
    }

    Variants(
        title = "Pairings",
        description = "One value that is entered in two parts, and so belongs in one field.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Amount and currency", detail = "editor weighted, select fixed") {
            var amount by remember { mutableStateOf("1250") }
            var currency by remember { mutableStateOf(setOf("EUR")) }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                TextSegment(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text("0.00") },
                )
                SegmentDivider()
                SelectSegment(
                    values = currency,
                    onChange = { currency = it },
                    options = Currencies,
                    width = 96.dp,
                )
            }
        }

        variant("Dialling code and number", detail = "select first, no divider") {
            var code by remember { mutableStateOf(setOf("+44")) }
            var number by remember { mutableStateOf("") }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                SelectSegment(
                    values = code,
                    onChange = { code = it },
                    options = DiallingCodes,
                    width = 92.dp,
                    leading = {
                        Icon(imageVector = ArecaIcons.Phone, contentDescription = null)
                    },
                )
                TextSegment(
                    value = number,
                    onValueChange = { number = it },
                    placeholder = { Text("7700 900000") },
                )
            }
        }

        variant("Measurement and unit", detail = "a divider between two halves of one number") {
            var length by remember { mutableStateOf("") }
            var unit by remember { mutableStateOf(setOf("cm")) }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                TextSegment(
                    value = length,
                    onValueChange = { length = it },
                    leading = {
                        Icon(imageVector = ArecaIcons.Ruler, contentDescription = null)
                    },
                    placeholder = { Text("Length") },
                )
                SegmentDivider()
                SelectSegment(
                    values = unit,
                    onChange = { unit = it },
                    options = Units,
                    width = 72.dp,
                )
            }
        }

        variant("Two editors", detail = "a range, where neither half is a picker") {
            var from by remember { mutableStateOf("") }
            var to by remember { mutableStateOf("") }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                TextSegment(
                    value = from,
                    onValueChange = { from = it },
                    placeholder = { Text("Min") },
                )
                SegmentDivider()
                TextSegment(
                    value = to,
                    onValueChange = { to = it },
                    placeholder = { Text("Max") },
                )
            }
        }
    }

    Variants(
        title = "Styles",
        description = "A composite takes the same presets a single-part field does.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in CompositeStyleOption.entries) {
            variant(name = option.name) {
                SampleComposite(style = option.resolve())
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty") { SampleComposite() }
        variant("Filled") { SampleComposite(amount = "1250") }
        variant("Error", detail = "one status colours the whole row") {
            SampleComposite(amount = "0", status = FieldStatus.Error)
        }
        variant("Disabled") { SampleComposite(amount = "1250", enabled = false) }
        variant("Read-only", detail = "neither half opens") {
            SampleComposite(amount = "1250", readOnly = true)
        }
    }

    Recipes {
        recipe(
            name = "Inside a field group",
            description = "Label above, error below — the composite behaves as one control.",
        ) {
            var amount by remember { mutableStateOf("") }
            var currency by remember { mutableStateOf(setOf("USD")) }

            FieldGroup(
                label = "Budget",
                required = true,
                assistive = "Monthly ceiling for this workspace.",
                error = if (amount.isNotEmpty() && amount.toIntOrNull() == null) {
                    "Enter a whole number."
                } else {
                    null
                },
            ) { field ->
                FieldRow(modifier = Modifier.fillMaxWidth(), field = field) {
                    TextSegment(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text("0.00") },
                    )
                    SegmentDivider()
                    SelectSegment(
                        values = currency,
                        onChange = { currency = it },
                        options = Currencies,
                        width = 96.dp,
                    )
                }
            }
        }

        recipe(
            name = "Label inside the container",
            description = "The label names the whole value, so it spans both segments.",
        ) {
            var amount by remember { mutableStateOf("") }
            var currency by remember { mutableStateOf(setOf("USD")) }

            FieldGroup(
                label = "Budget",
                labelPlacement = FieldLabelPlacement.Inside,
            ) { field ->
                FieldRow(modifier = Modifier.fillMaxWidth(), field = field) {
                    TextSegment(
                        value = amount,
                        onValueChange = { amount = it },
                        placeholder = { Text("0.00") },
                    )
                    SegmentDivider()
                    SelectSegment(
                        values = currency,
                        onChange = { currency = it },
                        options = Currencies,
                        width = 96.dp,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Unweighted select, long label",
            expectation = "The select is as wide as its selection and the editor gives up the " +
                "difference; nothing is clipped.",
        ) {
            var note by remember { mutableStateOf("") }
            var currency by remember { mutableStateOf(setOf("JPY")) }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                TextSegment(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = { Text("Reference") },
                )
                SegmentDivider()
                SelectSegment(
                    values = currency,
                    onChange = { currency = it },
                    options = Currencies,
                )
            }
        }

        case(
            name = "Three segments",
            expectation = "Dividers run the full height of the row whichever segment is tallest.",
        ) {
            var day by remember { mutableStateOf("") }
            var month by remember { mutableStateOf("") }
            var year by remember { mutableStateOf("") }

            FieldRow(modifier = Modifier.fillMaxWidth()) {
                TextSegment(
                    value = day,
                    onValueChange = { day = it },
                    placeholder = { Text("DD") },
                )
                SegmentDivider()
                TextSegment(
                    value = month,
                    onValueChange = { month = it },
                    placeholder = { Text("MM") },
                )
                SegmentDivider()
                TextSegment(
                    value = year,
                    onValueChange = { year = it },
                    placeholder = { Text("YYYY") },
                )
            }
        }
    }
}

/** Style presets a composite can be drawn with, as a selectable list. */
private enum class CompositeStyleOption {
    Outlined,
    Soft,
    Filled,
    Underlined,
}

/** Sizes a composite can be drawn at, as a selectable list. */
private enum class CompositeSizeOption {
    Small,
    Medium,
    Large,
}

/**
 * The amount-and-currency pairing, for sections that vary one thing about it.
 *
 * @param amount Value the editor starts with.
 * @param style Palette the row is drawn with.
 * @param status Validation outcome the row reports.
 * @param enabled Whether the row accepts input.
 * @param readOnly Whether the row's value can be changed.
 */
@Composable
private fun SampleComposite(
    amount: String = "",
    style: FieldStyle = FieldStyle.outlined(),
    status: FieldStatus = FieldStatus.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
) {
    var value by remember { mutableStateOf(amount) }
    var currency by remember { mutableStateOf(setOf("USD")) }

    FieldRow(
        modifier = Modifier.fillMaxWidth(),
        style = style,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
    ) {
        TextSegment(
            value = value,
            onValueChange = { value = it },
            placeholder = { Text("0.00") },
        )
        SegmentDivider()
        SelectSegment(
            values = currency,
            onChange = { currency = it },
            options = Currencies,
            width = 96.dp,
        )
    }
}

@Composable
private fun CompositeStyleOption.resolve(): FieldStyle = when (this) {
    CompositeStyleOption.Outlined -> FieldStyle.outlined()
    CompositeStyleOption.Soft -> FieldStyle.soft()
    CompositeStyleOption.Filled -> FieldStyle.filled()
    CompositeStyleOption.Underlined -> FieldStyle.underlined()
}

@Composable
private fun CompositeSizeOption.resolve(): FieldSize = when (this) {
    CompositeSizeOption.Small -> FieldSize.Small
    CompositeSizeOption.Medium -> FieldSize.Medium
    CompositeSizeOption.Large -> FieldSize.Large
}
