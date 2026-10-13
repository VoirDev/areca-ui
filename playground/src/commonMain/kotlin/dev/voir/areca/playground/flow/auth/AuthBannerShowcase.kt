package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Clock
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

/** Why the sample sign-in attempt failed, worded the way a service would word it. */
private const val FAILURE_MESSAGE = "That email and password don't match an account."

/** Headline of the sample rate-limit banner. */
private const val LIMIT_TITLE = "Too many attempts"

/** Sentence of the sample rate-limit banner. */
private const val LIMIT_MESSAGE = "For your security, wait 2 minutes or reset your password."


/** Showcase for [AuthBanner]. */
@Composable
fun AuthBannerShowcase() = ShowcaseScaffold(
    title = "Auth banner",
    summary = "Says why the last attempt failed, just above the button that tries again, and " +
        "announces it to assistive technology.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Toggle the failure to watch the banner expand in and collapse out.") {
        val failed by booleanControl(
            key = "failed",
            default = true,
            description = "Off passes message = null: the banner and its gap collapse away.",
        )
        val message by textControl("message", default = FAILURE_MESSAGE)
        val title by textControl(
            key = "title",
            default = "",
            description = "A headline over the message. Leave empty to pass null.",
        )
        val tone by enumControl(
            key = "tone",
            default = NoticeTone.Danger,
            options = listOf(NoticeTone.Danger, NoticeTone.Warning),
            optionLabel = { it.name },
        )
        val errorPrefix by booleanControl(
            key = "errorPrefix",
            default = true,
            label = "\"Error\" in contentDescription",
            description = "Not drawn: a screen reader hears \"Error\" before the title and the message.",
        )

        preview {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthBanner(
                    message = if (failed) message else null,
                    modifier = Modifier.fillMaxWidth(),
                    title = title.ifEmpty { null },
                    tone = tone,
                    contentDescription = if (errorPrefix) AuthSamples::errorDescription else ::plainDescription,
                )
                Button(text = "Sign in", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    Anatomy(description = "An icon and the message on a danger ground, with its own gap beneath.") {
        slot(
            name = "message",
            description = "Why the attempt failed, already worded. Drawn beside the alert icon.",
        ) {
            AuthBanner(message = FAILURE_MESSAGE, modifier = Modifier.fillMaxWidth())
        }
        slot(
            name = "contentDescription",
            description = "Not drawn: a screen reader hears \"${AuthSamples.errorDescription(null, FAILURE_MESSAGE)}\", " +
                "announced when the banner appears.",
        ) {
            AuthBanner(
                message = FAILURE_MESSAGE,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = AuthSamples::errorDescription,
            )
        }
    }

    Variants(
        title = "Tone",
        description = "Danger for a failed attempt; Warning, with a title and a clock, for a form that has to wait.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Danger", detail = "the default: fix something and try again") {
            AuthBanner(message = FAILURE_MESSAGE, modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Warning, titled", detail = "title = \"$LIMIT_TITLE\", icon = Clock") {
            AuthBanner(
                message = LIMIT_MESSAGE,
                modifier = Modifier.fillMaxWidth(),
                title = LIMIT_TITLE,
                tone = NoticeTone.Warning,
                icon = ArecaIcons.Clock,
            )
        }
        variant(name = "Danger, titled", detail = "a failure worth a headline") {
            AuthBanner(
                message = "We couldn't reach the server. Check your connection and try again.",
                modifier = Modifier.fillMaxWidth(),
                title = "Something went wrong",
            )
        }
    }

    Variants(
        title = "Content description",
        description = "Identical on screen; the difference is what assistive technology reads out.",
        layout = VariantLayout.Vertical,
    ) {
        variant(
            name = "With \"Error\"",
            detail = "Reads \"${AuthSamples.errorDescription(null, FAILURE_MESSAGE)}\"",
        ) {
            AuthBanner(
                message = FAILURE_MESSAGE,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = AuthSamples::errorDescription,
            )
        }
        variant(name = "Default", detail = "Reads the title, when there is one, and the message") {
            AuthBanner(message = FAILURE_MESSAGE, modifier = Modifier.fillMaxWidth())
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Failed", detail = "message given: the banner and the gap below it") {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthBanner(
                    message = FAILURE_MESSAGE,
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = AuthSamples::errorDescription,
                )
                Button(text = "Sign in", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
        variant(name = "No failure", detail = "message = null: nothing drawn, not even the gap") {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthBanner(message = null, modifier = Modifier.fillMaxWidth())
                Button(text = "Sign in", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    Recipes {
        recipe(
            name = "Above the button",
            description = "The last box, the failure and the button; press it to clear the failure and again to bring it back.",
        ) {
            var password by remember { mutableStateOf("") }
            var failure by remember { mutableStateOf<String?>(FAILURE_MESSAGE) }
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(ArecaTheme.spacing.xl2))
                AuthBanner(
                    message = failure,
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = AuthSamples::errorDescription,
                )
                Button(
                    text = "Sign in",
                    onClick = { failure = if (failure == null) FAILURE_MESSAGE else null },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long message",
            expectation = "Wraps onto as many lines as it needs; the icon stays centred against the text.",
        ) {
            AuthBanner(
                message = "We couldn't reach the sign-in service. Check your connection and try again. " +
                    "If this keeps happening, your network may be blocking the request.",
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Short message",
            expectation = "One word still spans the full width, so the banner lines up with the button.",
        ) {
            AuthBanner(message = "Offline.", modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the message wraps and nothing is cut.",
        ) {
            AuthBanner(message = FAILURE_MESSAGE, modifier = Modifier.width(220.dp))
        }
    }
}

/**
 * How the playground's banner reads without the "Error" lead: the title, when there is one, and
 * the message, as English sentences.
 *
 * @param title Headline over the message, or `null` for none.
 * @param message Failure being explained.
 * @return The spoken description.
 */
private fun plainDescription(title: String?, message: String): String =
    listOfNotNull(title, message).joinToString(". ")
