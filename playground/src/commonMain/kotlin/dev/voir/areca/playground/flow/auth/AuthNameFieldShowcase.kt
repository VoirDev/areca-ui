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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthNameField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The keyboard actions the playground field is offered with. */
private enum class NameImeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** Action handed to the field. */
    val action: ImeAction,
) {
    /** Another box follows, the default. */
    Next("Next", ImeAction.Next),

    /** The name is the last box, as on a screen that asks for it after sign-up. */
    Done("Done", ImeAction.Done),
}

/** The sample account holder's name. */
private const val SAMPLE_NAME = "Ada Lovelace"

/** What the sample application shows in the empty box. */
private const val NAME_PLACEHOLDER = "First and last name"

/** The sample application's complaint about the name, worded the way a product would word it. */
private const val NAME_ERROR = "Enter your name."

/** Height of the artwork a cell draws the field over; enough for the label, box and message. */
private val ArtworkHeight: Dp = 240.dp

/** Showcase for [AuthNameField]. */
@Composable
fun AuthNameFieldShowcase() = ShowcaseScaffold(
    title = "Auth name field",
    summary = "The full-name box of a sign-up form, with name autofill, capitalised words and an " +
        "error the application words.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type in the box; the error is a sample the application would supply.") {
        val label by textControl("label", default = "Full name")
        val placeholder by textControl(
            key = "placeholder",
            default = NAME_PLACEHOLDER,
            description = "Leave empty to pass null: the empty box stays blank.",
        )
        val error by booleanControl(
            key = "error",
            default = false,
            description = "Passes a sample complaint. The field checks nothing itself.",
        )
        val enabled by booleanControl("enabled", default = true)
        val imeAction by enumControl(
            key = "imeAction",
            default = NameImeOption.Next,
            options = NameImeOption.entries,
            optionLabel = { it.label },
        )

        preview {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                error = if (error) NAME_ERROR else null,
                enabled = enabled,
                imeAction = imeAction.action,
                label = label,
                placeholder = placeholder.ifEmpty { null },
            )
        }
    }

    Anatomy(description = "A labelled box and, when the application has a complaint, a line under it.") {
        slot(name = "label", description = "Above the box. Defaults to \"Full name\".") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        slot(name = "placeholder", description = "In the box while it is empty; nothing when null.") {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = NAME_PLACEHOLDER,
            )
        }
        slot(name = "error", description = "Under the box, already worded; turns the box to its error colours.") {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                error = NAME_ERROR,
            )
        }
    }

    Variants(
        title = "Label",
        description = "The label is the application's word for what it asks; the default suits a legal name.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Default", detail = "label = \"Full name\"") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Custom", detail = "label = \"What should we call you?\"") {
            var name by remember { mutableStateOf("Ada") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = "What should we call you?",
            )
        }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold the label turns light; the error keeps its colour.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: label OnContrast") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
            }
        }
        variant(name = "Over artwork / error", detail = "The complaint stays in the danger colour") {
            var name by remember { mutableStateOf("") }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthNameField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    error = NAME_ERROR,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Empty", detail = "Placeholder showing") {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = NAME_PLACEHOLDER,
            )
        }
        variant(name = "Filled", detail = "The keyboard capitalises each word") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Error", detail = "error = \"$NAME_ERROR\"") {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                error = NAME_ERROR,
            )
        }
        variant(name = "Disabled", detail = "enabled = false, as while an attempt is in flight") {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
    }

    Recipes {
        recipe(
            name = "Name, then email",
            description = "The top of a sign-up form: Next on the keyboard moves focus to the email box.",
        ) {
            val emailFocus = remember { FocusRequester() }
            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                AuthNameField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Next,
                    onImeAction = { emailFocus.requestFocus() },
                    placeholder = NAME_PLACEHOLDER,
                )
                AuthEmailField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    newAccount = true,
                    focusRequester = emailFocus,
                    placeholder = "name@example.com",
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long name",
            expectation = "Stays on one line and scrolls within the box; the box never grows taller.",
        ) {
            var name by remember {
                mutableStateOf("Augusta Ada King, Countess of Lovelace and Baroness Wentworth")
            }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Very long label",
            expectation = "The label wraps above the box rather than being cut.",
        ) {
            var name by remember { mutableStateOf(SAMPLE_NAME) }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Your name, exactly as it appears on the card or document you will verify with",
            )
        }
        case(
            name = "No placeholder",
            expectation = "placeholder = null and nothing typed: the box is blank and the label names it.",
        ) {
            var name by remember { mutableStateOf("") }
            AuthNameField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the box narrows and the complaint wraps.",
        ) {
            var name by remember { mutableStateOf("") }
            AuthNameField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.width(220.dp),
                error = "Enter your name as you would like it shown.",
            )
        }
    }
}
