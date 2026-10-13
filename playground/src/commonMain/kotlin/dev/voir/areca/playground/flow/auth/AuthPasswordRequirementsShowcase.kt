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
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthPasswordRequirements
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [AuthPasswordRequirements]. */
@Composable
fun AuthPasswordRequirementsShowcase() = ShowcaseScaffold(
    title = "Auth password requirements",
    summary = "The rules a new password has to meet, ticked off as it is typed and turned into the " +
        "reason after a refused submit.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type a password; checked is what the first submit flips.") {
        val checked by booleanControl(
            key = "checked",
            default = false,
            description = "Report unmet rules as Unmet rather than Pending.",
        )
        val columns by numberControl(
            key = "columns",
            default = 2f,
            range = 1f..2f,
            step = 1f,
            format = { it.toInt().toString() },
        )

        preview {
            var password by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    newAccount = true,
                )
                AuthPasswordRequirements(
                    requirements = AuthSamples.passwordRequirements(password, checked),
                    columns = columns.toInt(),
                    contentDescription = "Password requirements",
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Nothing typed", detail = "every rule pending") {
            AuthPasswordRequirements(requirements = AuthSamples.passwordRequirements("", checked = false))
        }
        variant("Typing", detail = "met rules ticked, the rest pending") {
            AuthPasswordRequirements(requirements = AuthSamples.passwordRequirements("Areca2026", checked = false))
        }
        variant("Refused", detail = "after a submit: unmet rules in danger") {
            AuthPasswordRequirements(requirements = AuthSamples.passwordRequirements("areca", checked = true))
        }
        variant("All met") {
            AuthPasswordRequirements(requirements = AuthSamples.passwordRequirements(AuthSamples.Password, checked = true))
        }
    }

    Variants(
        title = "Ground",
        description = "Over full-bleed artwork the pending and met rules turn light; unmet keeps its danger color.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthPasswordRequirements(requirements = AuthSamples.passwordRequirements("areca2026", checked = true))
            }
        }
    }

    EdgeCases {
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: two columns wrap their rules rather than cutting them.",
        ) {
            AuthPasswordRequirements(
                requirements = AuthSamples.passwordRequirements("Areca2026", checked = true),
                modifier = Modifier.width(220.dp),
            )
        }
        case(
            name = "One column",
            expectation = "columns = 1 for rules that are sentences.",
        ) {
            AuthPasswordRequirements(
                requirements = AuthSamples.passwordRequirements("Areca2026", checked = false),
                columns = 1,
            )
        }
    }
}
