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
import dev.voir.areca.flow.auth.part.AuthPasswordField
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
private enum class PasswordImeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** Action handed to the field. */
    val action: ImeAction,
) {
    /** The last box of the form, the default. */
    Done("Done", ImeAction.Done),

    /** A confirmation box follows. */
    Next("Next", ImeAction.Next),

    /** Pressing the key submits straight away. */
    Go("Go", ImeAction.Go),
}

/** The sample account's password. */
private const val SAMPLE_PASSWORD = "correct horse battery staple"

/** What the sample application shows in the empty box. */
private const val PASSWORD_PLACEHOLDER = "At least 8 characters"

/** The sample application's complaint about the password, worded the way a product would word it. */
private const val PASSWORD_ERROR = "Use at least 8 characters."

/** Height of the artwork a cell draws the field over; enough for the label, box and message. */
private val ArtworkHeight: Dp = 240.dp

/** Showcase for [AuthPasswordField]. */
@Composable
fun AuthPasswordFieldShowcase() = ShowcaseScaffold(
    title = "Auth password field",
    summary = "The password box of a sign-in or sign-up form, masked with a reveal toggle, with the " +
        "autofill type for recalling or choosing a password.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type in the box and press the eye; the error is a sample the application supplies.") {
        val label by textControl(
            key = "label",
            default = "Password",
            description = "Pass the confirmation label for the second box of a pair.",
        )
        val placeholder by textControl(
            key = "placeholder",
            default = PASSWORD_PLACEHOLDER,
            description = "Leave empty to pass null: the empty box stays blank.",
        )
        val error by booleanControl(
            key = "error",
            default = false,
            description = "Passes a sample complaint. The field checks nothing itself.",
        )
        val enabled by booleanControl("enabled", default = true)
        val newAccount by booleanControl(
            key = "newAccount",
            default = true,
            description = "Autofill offers to generate a password instead of filling the saved one. Not drawn.",
        )
        val imeAction by enumControl(
            key = "imeAction",
            default = PasswordImeOption.Done,
            options = PasswordImeOption.entries,
            optionLabel = { it.label },
        )
        val revealDescription by textControl(
            key = "revealDescription",
            default = "Show password",
            description = "Read by a screen reader on the eye while the password is masked.",
        )
        val hideDescription by textControl(
            key = "hideDescription",
            default = "Hide password",
            description = "Read by a screen reader on the eye while the password is shown.",
        )

        preview {
            var password by remember { mutableStateOf("") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                error = if (error) PASSWORD_ERROR else null,
                enabled = enabled,
                newAccount = newAccount,
                label = label,
                imeAction = imeAction.action,
                placeholder = placeholder.ifEmpty { null },
                revealDescription = revealDescription,
                hideDescription = hideDescription,
            )
        }
    }

    Anatomy(description = "A labelled, masked box with a reveal toggle, and a line under it when there is a complaint.") {
        slot(name = "label", description = "Above the box. Defaults to \"Password\".") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        slot(name = "placeholder", description = "In the box while it is empty; nothing when null.") {
            var password by remember { mutableStateOf("") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = PASSWORD_PLACEHOLDER,
            )
        }
        slot(
            name = "Reveal toggle",
            description = "Unmasks the value; labelled revealDescription, then hideDescription once pressed.",
        ) {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        slot(name = "error", description = "Under the box, already worded; turns the box to its error colours.") {
            var password by remember { mutableStateOf("secret") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                error = PASSWORD_ERROR,
            )
        }
    }

    Variants(
        title = "Recalling or choosing",
        description = "Identical on screen; newAccount changes only what a password manager offers.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "newAccount = false", detail = "ContentType.Password: fills the saved password") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "newAccount = true", detail = "ContentType.NewPassword: offers to generate one") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                newAccount = true,
            )
        }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold the label turns light; the error keeps its colour.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: label OnContrast") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        variant(name = "Over artwork / error", detail = "The complaint stays in the danger colour") {
            var password by remember { mutableStateOf("secret") }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    error = PASSWORD_ERROR,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Empty", detail = "Placeholder showing") {
            var password by remember { mutableStateOf("") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = PASSWORD_PLACEHOLDER,
            )
        }
        variant(name = "Filled", detail = "Masked until the eye is pressed") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Error", detail = "error = \"$PASSWORD_ERROR\"") {
            var password by remember { mutableStateOf("secret") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                error = PASSWORD_ERROR,
            )
        }
        variant(name = "Disabled", detail = "enabled = false, as while an attempt is in flight") {
            var password by remember { mutableStateOf(SAMPLE_PASSWORD) }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
    }

    Recipes {
        recipe(
            name = "Password and confirmation",
            description = "Both boxes choose a password; Next on the first moves focus to the second.",
        ) {
            val confirmationFocus = remember { FocusRequester() }
            var password by remember { mutableStateOf("") }
            var confirmation by remember { mutableStateOf("") }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    newAccount = true,
                    imeAction = ImeAction.Next,
                    onImeAction = { confirmationFocus.requestFocus() },
                    placeholder = PASSWORD_PLACEHOLDER,
                )
                AuthPasswordField(
                    value = confirmation,
                    onValueChange = { confirmation = it },
                    modifier = Modifier.fillMaxWidth(),
                    newAccount = true,
                    label = "Confirm password",
                    focusRequester = confirmationFocus,
                    imeAction = ImeAction.Done,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long password",
            expectation = "Stays on one line and scrolls within the box; the eye keeps its place.",
        ) {
            var password by remember {
                mutableStateOf("correct horse battery staple and a second horse for good measure")
            }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Very long error",
            expectation = "The complaint wraps under the box onto as many lines as it needs.",
        ) {
            var password by remember { mutableStateOf("password123") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                newAccount = true,
                error = "This password has appeared in a known data breach. Choose one you have not " +
                    "used anywhere else.",
            )
        }
        case(
            name = "No placeholder",
            expectation = "placeholder = null and nothing typed: the box is blank and the label names it.",
        ) {
            var password by remember { mutableStateOf("") }
            AuthPasswordField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the box narrows around the eye and the complaint wraps.",
        ) {
            var password by remember { mutableStateOf("secret") }
            AuthPasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.width(220.dp),
                error = PASSWORD_ERROR,
            )
        }
    }
}
