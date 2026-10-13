package dev.voir.areca.playground.foundation.feedback

import androidx.compose.foundation.layout.Arrangement
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
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.Requirement
import dev.voir.areca.foundation.feedback.RequirementList
import dev.voir.areca.foundation.feedback.RequirementState
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The four rules a sample service holds a new password to, in reading order. */
private val PasswordRules = listOf(
    "12 or more characters",
    "Upper and lower case",
    "A number",
    "A symbol",
)

/**
 * The sample service's password rules, checked against a value.
 *
 * @param password Password as typed.
 * @param checked Whether an unmet rule is reported as a problem rather than as guidance.
 * @return One requirement per rule.
 */
private fun passwordRequirements(password: String, checked: Boolean): List<Requirement> {
    val unmet = if (checked) RequirementState.Unmet else RequirementState.Pending
    val met = listOf(
        password.length >= 12,
        password.any(Char::isUpperCase) && password.any(Char::isLowerCase),
        password.any(Char::isDigit),
        password.any { !it.isLetterOrDigit() },
    )
    return PasswordRules.zip(met) { text, isMet -> Requirement(text, if (isMet) RequirementState.Met else unmet) }
}

/** Showcase for [RequirementList]. */
@Composable
fun RequirementListShowcase() = ShowcaseScaffold(
    title = "Requirement list",
    summary = "The rules a value has to meet, each marked as pending, met or unmet as it is typed.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type into the box to tick rules off; the checked switch is what a first submit flips.") {
        val columns by numberControl(
            key = "columns",
            default = 2f,
            range = 1f..3f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val checked by booleanControl(
            key = "checked",
            default = false,
            description = "Report unmet rules as Unmet rather than Pending.",
        )

        preview {
            var password by remember { mutableStateOf("Areca2026") }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                FieldGroup(label = "Password", modifier = Modifier.fillMaxWidth()) { field ->
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        field = field,
                        singleLine = true,
                    )
                }
                RequirementList(
                    requirements = passwordRequirements(password, checked),
                    columns = columns.toInt(),
                    contentDescription = "Password requirements",
                )
            }
        }
    }

    Variants(
        title = "Columns",
        description = "Rules fill each row before the next; a short last row keeps its column.",
        layout = VariantLayout.Vertical,
    ) {
        for (count in 1..3) {
            variant(name = "columns = $count") {
                RequirementList(requirements = passwordRequirements("Areca2026", checked = true), columns = count)
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Pending", detail = "before the first submit: quiet guidance") {
            RequirementList(requirements = passwordRequirements("", checked = false), columns = 2)
        }
        variant("Met and pending", detail = "while typing") {
            RequirementList(requirements = passwordRequirements("areca2026", checked = false), columns = 2)
        }
        variant("Met and unmet", detail = "after a refused submit") {
            RequirementList(requirements = passwordRequirements("areca2026", checked = true), columns = 2)
        }
        variant("All met") {
            RequirementList(requirements = passwordRequirements("Areca#2026-sign", checked = true), columns = 2)
        }
    }

    Recipes {
        recipe(
            name = "Checked on submit",
            description = "Guidance while typing; pressing the button turns every unmet rule into the reason it was refused.",
        ) {
            var password by remember { mutableStateOf("") }
            var checked by remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                FieldGroup(label = "New password", modifier = Modifier.fillMaxWidth()) { field ->
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        field = field,
                        singleLine = true,
                    )
                }
                RequirementList(requirements = passwordRequirements(password, checked), columns = 2)
                Button(text = "Set password", onClick = { checked = true }, modifier = Modifier.fillMaxWidth())
            }
        }
        recipe(
            name = "Username rules",
            description = "Not only passwords: any constraint someone can satisfy while typing.",
        ) {
            RequirementList(
                requirements = listOf(
                    Requirement("Between 5 and 32 characters", RequirementState.Met),
                    Requirement("Letters, digits and underscores only", RequirementState.Unmet),
                    Requirement("Starts with a letter", RequirementState.Met),
                ),
            )
        }
    }

    EdgeCases {
        case(
            name = "Long rule in two columns",
            expectation = "Wraps within its column; the mark stays beside the first line and the row grows.",
        ) {
            RequirementList(
                requirements = listOf(
                    Requirement("At least one character from a script other than the one the rest is in", RequirementState.Pending),
                    Requirement("A number", RequirementState.Met),
                ),
                columns = 2,
            )
        }
        case(name = "Empty list", expectation = "Nothing is drawn and no space is taken.") {
            RequirementList(requirements = emptyList())
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 200dp: two columns still split evenly and wrap their text.",
        ) {
            RequirementList(
                requirements = passwordRequirements("Areca2026", checked = true),
                columns = 2,
                modifier = Modifier.width(200.dp),
            )
        }
    }
}
