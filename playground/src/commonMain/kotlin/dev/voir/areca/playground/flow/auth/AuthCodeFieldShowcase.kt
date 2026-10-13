package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthCodeField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The complaint the sample service words for a code that does not match. */
private const val WRONG_CODE = AuthSamples.WrongCodeMessage

/** The complaint the sample service words for a code that has run out. */
private const val EXPIRED_CODE = AuthSamples.ExpiredCodeMessage

/** Showcase for [AuthCodeField]. */
@Composable
fun AuthCodeFieldShowcase() = ShowcaseScaffold(
    title = "Auth code field",
    summary = "The box for a code sent by email or text message, with one-time-code autofill and a " +
        "callback when the last digit lands.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type six digits: onComplete verifies at once. 123456 passes, 000000 has expired.") {
        val length by numberControl(
            key = "length",
            default = 6f,
            range = 4f..8f,
            step = 1f,
            format = { "${it.toInt()} digits" },
        )
        val grouped by booleanControl("grouped", default = false, description = "groupSize = half the length.")
        val assistive by textControl(
            key = "assistive",
            default = "",
            description = "Hint under the cells while there is no error. Leave empty to pass null.",
        )
        val enabled by booleanControl("enabled", default = true)

        preview {
            var code by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }

            AuthCodeField(
                value = code,
                onValueChange = {
                    code = it
                    error = null
                },
                onComplete = { error = AuthSamples.codeComplaint(it) },
                modifier = Modifier.fillMaxWidth(),
                length = length.toInt(),
                error = error,
                enabled = enabled,
                assistive = assistive.ifEmpty { null },
                groupSize = if (grouped) length.toInt() / 2 else null,
            )
        }
    }

    Variants(
        title = "Grouping",
        description = "groupSize matches how the message prints the code.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "One run", detail = "groupSize = null") { SampleCodeField(initial = "482913") }
        variant(name = "Two groups of three", detail = "groupSize = 3") { SampleCodeField(initial = "482913", groupSize = 3) }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty") { SampleCodeField() }
        variant("Typing") { SampleCodeField(initial = "482") }
        variant("Wrong code", detail = "error given: every cell turns") { SampleCodeField(initial = "482913", error = WRONG_CODE) }
        variant("Expired code") { SampleCodeField(initial = "000000", error = EXPIRED_CODE) }
        variant("Verifying", detail = "enabled = false") { SampleCodeField(initial = "123456", enabled = false) }
        variant("With a hint", detail = "assistive given, no error") {
            SampleCodeField(assistive = "It expires in 10 minutes.")
        }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold the label and hint turn light; the cells keep their own ground.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                SampleCodeField(initial = "4829", assistive = "It expires in 10 minutes.")
            }
        }
    }

    Recipes {
        recipe(
            name = "With a Verify button",
            description = "Verifies on the last digit and on the button, for anyone who wants to press something.",
        ) {
            var code by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl2)) {
                AuthCodeField(
                    value = code,
                    onValueChange = {
                        code = it
                        error = null
                    },
                    onComplete = { error = AuthSamples.codeComplaint(it) },
                    modifier = Modifier.fillMaxWidth(),
                    error = error,
                )
                Button(
                    text = "Verify",
                    onClick = {
                        error = if (code.length < 6) "Enter the 6-digit code from your email." else AuthSamples.codeComplaint(code)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Eight digits in a narrow container",
            expectation = "Constrained to 260dp: eight cells shrink evenly and the digits stay legible.",
        ) {
            SampleCodeField(initial = "48291357", length = 8, modifier = Modifier.width(260.dp))
        }
        case(
            name = "Long complaint",
            expectation = "The message wraps under the cells; the row does not move.",
        ) {
            SampleCodeField(
                initial = "482913",
                error = "That code isn't right. Codes are single-use, so if you asked for a new one, " +
                    "only the newest works.",
            )
        }
    }
}

/**
 * One live code field with a fixed configuration.
 *
 * @param initial Code the field starts with.
 * @param modifier Modifier applied to the field.
 * @param length Number of cells.
 * @param error Complaint to show, or `null`.
 * @param enabled Whether the cells accept input.
 * @param assistive Hint under the cells, or `null`.
 * @param groupSize Cells per group, or `null`.
 */
@Composable
private fun SampleCodeField(
    initial: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    length: Int = 6,
    error: String? = null,
    enabled: Boolean = true,
    assistive: String? = null,
    groupSize: Int? = null,
) {
    var code by remember { mutableStateOf(initial) }
    AuthCodeField(
        value = code,
        onValueChange = { code = it },
        modifier = modifier,
        length = length,
        error = error,
        enabled = enabled,
        assistive = assistive,
        groupSize = groupSize,
    )
}
