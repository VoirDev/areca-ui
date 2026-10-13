package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.part.AuthIdentityChip
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** The sample account every page under `flow/auth` signs in as. */
private const val SAMPLE_EMAIL = "ada@example.com"

/** Showcase for [AuthIdentityChip]. */
@Composable
fun AuthIdentityChipShowcase() = ShowcaseScaffold(
    title = "Auth identity chip",
    summary = "The account a step of an email-first flow is about, with the way back to choose " +
        "another right beside it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press Change to count the presses; turn change off to pass onChange = null.") {
        val identifier by textControl("identifier", default = SAMPLE_EMAIL)
        val change by booleanControl("change", default = true, description = "Off passes onChange = null.")
        val verified by booleanControl("verified", default = false)
        val enabled by booleanControl("enabled", default = true)
        val label by textControl("label", default = "", description = "Drawn above like a field's label. Leave empty to pass null.")

        preview {
            var presses by remember { mutableIntStateOf(0) }
            AuthIdentityChip(
                identifier = identifier,
                onChange = if (change) ({ presses++ }) else null,
                changeLabel = if (presses == 0) "Change" else "Change ($presses)",
                onChangeLabel = "Use a different email",
                verified = verified,
                enabled = enabled,
                label = label.ifEmpty { null },
            )
        }
    }

    Anatomy(description = "The address, a tick when it is confirmed, and the way back.") {
        slot(name = "identifier", description = "What was typed on the previous screen, shortened in the middle when long.") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL)
        }
        slot(name = "verified", description = "A success tick, read as \"Verified\".") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, verified = true)
        }
        slot(name = "onChange", description = "A plain button back to the address step, its press labelled by onChangeLabel.") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, onChange = {}, onChangeLabel = "Use a different email")
        }
        slot(name = "label", description = "What the identifier is, above the chip the way a field's label is; drawn only when given.") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, label = "Account")
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Password step", detail = "onChange given") { AuthIdentityChip(identifier = SAMPLE_EMAIL, onChange = {}) }
        variant("Attempt in flight", detail = "enabled = false") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, onChange = {}, enabled = false)
        }
        variant("Create-password step", detail = "verified, no way back") {
            AuthIdentityChip(identifier = "new@example.com", verified = true)
        }
        variant("Emailed-link page", detail = "label given, no way back") {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, label = "Account")
        }
    }

    EdgeCases {
        case(
            name = "Very long address",
            expectation = "One line, shortened in the middle, so the start and the domain both stay visible.",
        ) {
            AuthIdentityChip(
                identifier = "augusta.ada.king.countess.of.lovelace@analytical-engine-society.example.com",
                onChange = {},
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the button keeps its width and the address gives way.",
        ) {
            AuthIdentityChip(identifier = SAMPLE_EMAIL, onChange = {}, modifier = Modifier.width(220.dp))
        }
        case(
            name = "Phone number",
            expectation = "Any identifier works; the chip does not assume an email.",
        ) {
            AuthIdentityChip(identifier = "+44 20 7946 0958", onChange = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
