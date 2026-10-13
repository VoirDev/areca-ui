package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.part.AuthResendCode
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/** The words before the link on every sample. */
private const val PROMPT = "Didn't get it?"

/** The confirmation the sample application words after a resend. */
private const val SENT = "New code sent."

/** The countdown the sample application words while a resend is not allowed. */
private const val COOLDOWN = "You can ask for another in 0:59."

/** Showcase for [AuthResendCode]. */
@Composable
fun AuthResendCodeShowcase() = ShowcaseScaffold(
    title = "Auth resend code",
    summary = "The way to ask for another code, the confirmation that one went out, and the wait " +
        "before the next.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Press the link: the sample application confirms and starts a cooldown.") {
        val prompt by textControl("prompt", default = PROMPT, description = "Leave empty to pass null.")
        val enabled by booleanControl("enabled", default = true)
        val alignment by enumControl(
            key = "textAlign",
            default = TextAlign.Center,
            options = listOf(TextAlign.Start, TextAlign.Center),
            optionLabel = { if (it == TextAlign.Start) "Start" else "Center" },
        )

        preview {
            var sent by remember { mutableStateOf(false) }
            AuthResendCode(
                onResend = { sent = true },
                prompt = prompt.ifEmpty { null },
                sent = if (sent) SENT else null,
                cooldown = if (sent) COOLDOWN else null,
                enabled = enabled,
                textAlign = alignment,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Nothing sent yet", detail = "sent = null, cooldown = null") {
            AuthResendCode(onResend = {}, prompt = PROMPT)
        }
        variant("Just sent, waiting", detail = "sent and cooldown given: the link is gone") {
            AuthResendCode(onResend = {}, prompt = PROMPT, sent = SENT, cooldown = COOLDOWN)
        }
        variant("Waiting", detail = "cooldown only") {
            AuthResendCode(onResend = {}, prompt = PROMPT, cooldown = "You can ask for another in 0:12.")
        }
        variant("Sent, can ask again", detail = "sent only: confirmation and link") {
            AuthResendCode(onResend = {}, prompt = PROMPT, sent = SENT)
        }
        variant("Verifying", detail = "enabled = false") {
            AuthResendCode(onResend = {}, prompt = PROMPT, enabled = false)
        }
        variant("Link alone", detail = "prompt = null") {
            AuthResendCode(onResend = {})
        }
    }

    EdgeCases {
        case(
            name = "Long translation",
            expectation = "The sentence wraps as a sentence, the link inside it.",
        ) {
            AuthResendCode(
                onResend = {},
                prompt = "Haben Sie keine E-Mail erhalten? Prüfen Sie auch Ihren Spam-Ordner.",
                actionText = "Neuen Code senden",
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 180dp: both lines wrap and stay centred.",
        ) {
            AuthResendCode(
                onResend = {},
                prompt = PROMPT,
                sent = SENT,
                modifier = Modifier.width(180.dp),
            )
        }
    }
}
