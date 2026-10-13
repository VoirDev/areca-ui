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
private enum class EmailImeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** Action handed to the field. */
    val action: ImeAction,
) {
    /** Another box follows, the default. */
    Next("Next", ImeAction.Next),

    /** The email is the last box, as on a passwordless or recovery form. */
    Done("Done", ImeAction.Done),

    /** Pressing the key submits straight away. */
    Go("Go", ImeAction.Go),
}

/** The sample account's address. */
private const val SAMPLE_EMAIL = "ada@analytical.engine"

/** What the sample application shows in the empty box. */
private const val EMAIL_PLACEHOLDER = "name@example.com"

/** The sample application's complaint about the address, worded the way a product would word it. */
private const val EMAIL_ERROR = "That does not look like an email address."

/** Height of the artwork a cell draws the field over; enough for the label, box and message. */
private val ArtworkHeight: Dp = 240.dp

/** Showcase for [AuthEmailField]. */
@Composable
fun AuthEmailFieldShowcase() = ShowcaseScaffold(
    title = "Auth email field",
    summary = "The email box of a sign-in or sign-up form, with the email keyboard, the right " +
        "autofill type for either and an error the application words.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Type in the box; the error is a sample the application would supply.") {
        val label by textControl("label", default = "Email")
        val placeholder by textControl(
            key = "placeholder",
            default = EMAIL_PLACEHOLDER,
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
            default = false,
            description = "Autofill offers to save this address instead of filling a saved one. Not drawn.",
        )
        val imeAction by enumControl(
            key = "imeAction",
            default = EmailImeOption.Next,
            options = EmailImeOption.entries,
            optionLabel = { it.label },
        )

        preview {
            var email by remember { mutableStateOf("") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                error = if (error) EMAIL_ERROR else null,
                enabled = enabled,
                newAccount = newAccount,
                imeAction = imeAction.action,
                label = label,
                placeholder = placeholder.ifEmpty { null },
            )
        }
    }

    Anatomy(description = "A labelled box and, when the application has a complaint, a line under it.") {
        slot(name = "label", description = "Above the box. Defaults to \"Email\".") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        slot(name = "placeholder", description = "In the box while it is empty; nothing when null.") {
            var email by remember { mutableStateOf("") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = EMAIL_PLACEHOLDER,
            )
        }
        slot(name = "error", description = "Under the box, already worded; turns the box to its error colours.") {
            var email by remember { mutableStateOf("ada.analytical.engine") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                error = EMAIL_ERROR,
            )
        }
    }

    Variants(
        title = "Signing in or creating an account",
        description = "Identical on screen; newAccount changes only what a password manager offers.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "newAccount = false", detail = "ContentType.EmailAddress: fills a saved address") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "newAccount = true", detail = "ContentType.NewUsername: offers to save this one") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
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
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: label OnContrast") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
            }
        }
        variant(name = "Over artwork / error", detail = "The complaint stays in the danger colour") {
            var email by remember { mutableStateOf("ada.analytical.engine") }
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.FullBleed),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthEmailField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    error = EMAIL_ERROR,
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Empty", detail = "Placeholder showing") {
            var email by remember { mutableStateOf("") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = EMAIL_PLACEHOLDER,
            )
        }
        variant(name = "Filled") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Error", detail = "error = \"$EMAIL_ERROR\"") {
            var email by remember { mutableStateOf("ada.analytical.engine") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                error = EMAIL_ERROR,
            )
        }
        variant(name = "Disabled", detail = "enabled = false, as while an attempt is in flight") {
            var email by remember { mutableStateOf(SAMPLE_EMAIL) }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
    }

    Recipes {
        recipe(
            name = "Email, then password",
            description = "Next on the keyboard moves focus to the password box through its FocusRequester.",
        ) {
            val passwordFocus = remember { FocusRequester() }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                AuthEmailField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    imeAction = ImeAction.Next,
                    onImeAction = { passwordFocus.requestFocus() },
                    placeholder = EMAIL_PLACEHOLDER,
                )
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    focusRequester = passwordFocus,
                    imeAction = ImeAction.Done,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long address",
            expectation = "Stays on one line and scrolls within the box; the box never grows taller.",
        ) {
            var email by remember {
                mutableStateOf("augusta.ada.king.countess.of.lovelace@analytical-engine-society.org.uk")
            }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Very long error",
            expectation = "The complaint wraps under the box onto as many lines as it needs.",
        ) {
            var email by remember { mutableStateOf("ada@analytical.engine") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                error = "This address belongs to a workspace that signs in through its own provider. " +
                    "Use the Continue with Google button instead.",
            )
        }
        case(
            name = "No placeholder",
            expectation = "placeholder = null and nothing typed: the box is blank and the label names it.",
        ) {
            var email by remember { mutableStateOf("") }
            AuthEmailField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the box narrows and the complaint wraps.",
        ) {
            var email by remember { mutableStateOf("ada.analytical.engine") }
            AuthEmailField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.width(220.dp),
                error = EMAIL_ERROR,
            )
        }
    }
}
