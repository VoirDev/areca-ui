package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.OtpField
import dev.voir.areca.foundation.field.OtpFieldSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Cell styles an [OtpField] takes from [FieldStyle], as a selectable list. */
private enum class OtpStyleOption {
    Outlined,
    Soft,
    Filled,
    Underlined,
}

/** Sizes offered by [OtpFieldSize], as a selectable list. */
private enum class OtpSizeOption {
    Small,
    Medium,
    Large,
}

/**
 * Code lengths services actually send, as a selectable list.
 *
 * @property length Number of cells.
 */
private enum class OtpLengthOption(val length: Int) {
    Four(4),
    Six(6),
    Eight(8),
}

/** The code a filled sample shows — six digits, as a service would send them. */
private const val SAMPLE_CODE = "482913"

/** Showcase for [OtpField]. */
@Composable
fun OtpFieldShowcase() = ShowcaseScaffold(
    title = "OTP field",
    summary = "A one-time code typed one character to a cell, over a single editor that still " +
        "pastes, autofills and reads as one field.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type, paste \"123-456\", or backspace: the ringed cell follows the caret.") {
        val length by enumControl(
            key = "length",
            default = OtpLengthOption.Six,
            options = OtpLengthOption.entries,
            optionLabel = { it.length.toString() },
        )
        val style by enumControl(
            key = "style",
            default = OtpStyleOption.Outlined,
            options = OtpStyleOption.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = OtpSizeOption.Medium,
            options = OtpSizeOption.entries,
            optionLabel = { it.name },
        )
        val status by enumControl(
            key = "status",
            default = FieldStatus.Default,
            options = FieldStatus.entries,
            optionLabel = { it.name },
        )
        val stretch by booleanControl("stretch", default = false, description = "Cells share the width.")
        val grouped by booleanControl("grouped", default = false, description = "groupSize = half the length.")
        val masked by booleanControl("masked", default = false, description = "Dots, for a PIN.")
        val placeholder by booleanControl("placeholder", default = false, description = "A dash in empty cells.")
        val enabled by booleanControl("enabled", default = true)

        preview {
            var code by remember { mutableStateOf("") }
            var completed by remember { mutableStateOf<String?>(null) }

            FieldGroup(
                label = "Verification code",
                assistive = completed?.let { "onComplete(\"$it\")" } ?: "Enter the ${length.length}-digit code.",
                status = status,
                error = if (status == FieldStatus.Error) "That code isn't right." else null,
                modifier = Modifier.fillMaxWidth(),
            ) { field ->
                OtpField(
                    value = code,
                    onValueChange = {
                        code = it
                        completed = null
                    },
                    onComplete = { completed = it },
                    modifier = if (stretch) Modifier.fillMaxWidth() else Modifier,
                    length = length.length,
                    style = style.resolve(),
                    size = size.resolve(),
                    field = field,
                    enabled = enabled,
                    masked = masked,
                    stretch = stretch,
                    groupSize = if (grouped) length.length / 2 else null,
                    placeholder = if (placeholder) '–' else null,
                )
            }
        }
    }

    Variants(
        title = "Styles",
        description = "The cells are field containers: every FieldStyle preset applies, status and all.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in OtpStyleOption.entries) {
            variant(name = option.name) {
                SampleOtp(initial = "482", style = option.resolve())
            }
        }
    }

    Sizes(
        description = "Cell width and height, gaps and the digit's type step up together.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Small", detail = "40 × 48dp cells") { SampleOtp(initial = SAMPLE_CODE, size = OtpFieldSize.Small) }
        variant(name = "Medium", detail = "48 × 56dp cells") { SampleOtp(initial = SAMPLE_CODE, size = OtpFieldSize.Medium) }
        variant(name = "Large", detail = "56 × 64dp cells, four digits") {
            SampleOtp(initial = "4829", length = 4, size = OtpFieldSize.Large)
        }
    }

    Variants(
        title = "Grouping",
        description = "groupSize splits the row the way the message prints the code.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "groupSize = null") { SampleOtp(initial = SAMPLE_CODE) }
        variant(name = "groupSize = 3", detail = "a wider gap") { SampleOtp(initial = SAMPLE_CODE, groupSize = 3) }
        variant(name = "groupSize = 3, separator", detail = "a dash between groups") {
            SampleOtp(
                initial = SAMPLE_CODE,
                groupSize = 3,
                separator = { Text("–", variant = TextVariant.HeadingSmall, color = TextColor.Muted) },
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty") { SampleOtp() }
        variant("Partly filled") { SampleOtp(initial = "482") }
        variant("Filled") { SampleOtp(initial = SAMPLE_CODE) }
        variant("Error", detail = "every cell reports it") { SampleOtp(initial = SAMPLE_CODE, status = FieldStatus.Error) }
        variant("Success") { SampleOtp(initial = SAMPLE_CODE, status = FieldStatus.Success) }
        variant("Disabled", detail = "while the code is verified") { SampleOtp(initial = SAMPLE_CODE, enabled = false) }
        variant("Read-only") { SampleOtp(initial = SAMPLE_CODE, readOnly = true) }
        variant("Masked", detail = "a PIN") { SampleOtp(initial = "4829", length = 4, masked = true) }
    }

    Recipes {
        recipe(
            name = "Verify on the last digit",
            description = "onComplete checks the code the moment the last cell fills; 123456 is right.",
        ) {
            var code by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }
            val verify = { value: String ->
                error = if (value == "123456") null else "That code isn't right. Check your email and try again."
            }
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl2)) {
                FieldGroup(label = "Verification code", error = error, modifier = Modifier.fillMaxWidth()) { field ->
                    OtpField(
                        value = code,
                        onValueChange = {
                            code = it
                            error = null
                        },
                        onComplete = verify,
                        modifier = Modifier.fillMaxWidth(),
                        field = field,
                        stretch = true,
                    )
                }
                Button(text = "Verify", onClick = { verify(code) }, modifier = Modifier.fillMaxWidth())
            }
        }
        recipe(
            name = "Alphanumeric code",
            description = "A letter-and-digit code: a filter that accepts both and a keyboard that offers letters.",
        ) {
            var code by remember { mutableStateOf("") }
            FieldGroup(label = "Recovery code", modifier = Modifier.fillMaxWidth()) { field ->
                OtpField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    modifier = Modifier.fillMaxWidth(),
                    length = 8,
                    field = field,
                    stretch = true,
                    groupSize = 4,
                    filter = Char::isLetterOrDigit,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp and stretched: six cells shrink evenly, and the digits stay centred.",
        ) {
            SampleOtp(initial = SAMPLE_CODE, modifier = Modifier.width(240.dp), stretch = true)
        }
        case(
            name = "Over-long value from state",
            expectation = "A value longer than the field draws only its first cells.",
        ) {
            SampleOtp(initial = "4829135555")
        }
        case(
            name = "Value with rejected characters",
            expectation = "\"48-29\" from state draws as 4829; the dash never reaches a cell.",
        ) {
            SampleOtp(initial = "48-29")
        }
        case(
            name = "One cell",
            expectation = "length = 1 draws a single cell, for a one-character confirmation.",
        ) {
            SampleOtp(initial = "", length = 1)
        }
    }
}

/**
 * One live code field, for the cells that show a fixed configuration.
 *
 * @param initial Code the field starts with.
 * @param modifier Modifier applied to the field.
 * @param length Number of cells.
 * @param style Cell style.
 * @param size Cell metrics.
 * @param status Validation outcome.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the value can be changed.
 * @param masked Whether characters are drawn as dots.
 * @param stretch Whether the cells share the width.
 * @param groupSize Cells per group, or `null` for one run.
 * @param separator Drawn between groups, or `null` for a wider gap.
 */
@Composable
private fun SampleOtp(
    initial: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    length: Int = 6,
    style: FieldStyle = FieldStyle.outlined(),
    size: OtpFieldSize = OtpFieldSize.Default,
    status: FieldStatus = FieldStatus.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    masked: Boolean = false,
    stretch: Boolean = false,
    groupSize: Int? = null,
    separator: (@Composable () -> Unit)? = null,
) {
    var code by remember { mutableStateOf(initial) }

    OtpField(
        value = code,
        onValueChange = { code = it },
        modifier = if (stretch) modifier else Modifier,
        length = length,
        style = style,
        size = size,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        masked = masked,
        stretch = stretch,
        groupSize = groupSize,
        separator = separator,
    )
}

/**
 * The [FieldStyle] preset this option names.
 *
 * @return The preset.
 */
@Composable
private fun OtpStyleOption.resolve(): FieldStyle = when (this) {
    OtpStyleOption.Outlined -> FieldStyle.outlined()
    OtpStyleOption.Soft -> FieldStyle.soft()
    OtpStyleOption.Filled -> FieldStyle.filled()
    OtpStyleOption.Underlined -> FieldStyle.underlined()
}

/**
 * The [OtpFieldSize] preset this option names.
 *
 * @return The preset.
 */
@Composable
private fun OtpSizeOption.resolve(): OtpFieldSize = when (this) {
    OtpSizeOption.Small -> OtpFieldSize.Small
    OtpSizeOption.Medium -> OtpFieldSize.Medium
    OtpSizeOption.Large -> OtpFieldSize.Large
}
