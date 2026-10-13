package dev.voir.areca.playground.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.voir.areca.foundation.overlay.ConfirmationDialog
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import kotlinx.coroutines.delay

/** Showcase for [ConfirmationDialog]. */
@Composable
fun ConfirmationDialogShowcase() = ShowcaseScaffold(
    title = "Confirmation dialog",
    summary = "Blocking prompt for an action that cannot easily be undone.",
    module = "foundation",
) {
    Playground(description = "Open the dialog; it dismisses only through its own buttons.") {
        val title by textControl("title", default = "Delete workspace?")
        val body by textControl(
            key = "text",
            default = "This removes every project and cannot be undone.",
        )
        val confirmLabel by textControl("confirmText", default = "Delete")
        val withDecline by booleanControl("declineText", default = true, label = "decline button")
        val withLeading by booleanControl("leading", default = true, label = "leading icon")
        val slowConfirm by booleanControl(
            key = "slow confirm",
            default = false,
            description = "onConfirm is suspending — the dialog shows progress until it returns.",
        )

        var open by remember { mutableStateOf(false) }

        actionControl(key = "open", buttonLabel = "Open dialog", onInvoke = { open = true })

        preview {
            Text(
                text = if (open) "Dialog is open" else "Dialog is closed",
                variant = TextVariant.BodySmall,
                color = TextColor.Muted,
            )

            if (open) {
                ConfirmationDialog(
                    title = { Text(title) },
                    text = { Text(body) },
                    confirmText = { Text(confirmLabel) },
                    onConfirm = {
                        if (slowConfirm) delay(1500)
                        open = false
                    },
                    declineText = if (withDecline) ({ Text("Cancel") }) else null,
                    onDecline = if (withDecline) ({ open = false }) else null,
                    leading = if (withLeading) {
                        {
                            FeaturedIcon(
                                imageVector = ArecaIcons.AlertTriangle,
                                contentDescription = null,
                                style = FeaturedIconStyle.soft(ActionColor.Danger),
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Destructive confirmation",
            description = "Danger icon, explicit verb on the confirm button, cancel available.",
        ) {
            DialogExample(
                trigger = "Delete workspace",
                title = "Delete workspace?",
                body = "This removes every project and cannot be undone.",
                confirm = "Delete",
                decline = "Cancel",
                danger = true,
            )
        }
        recipe(
            name = "Acknowledgement",
            description = "One button, nothing to decline — the user just has to read it.",
        ) {
            DialogExample(
                trigger = "Show notice",
                title = "Session expired",
                body = "Sign in again to continue where you left off.",
                confirm = "Sign in",
                decline = null,
                danger = false,
            )
        }
    }

    EdgeCases {
        case(
            name = "Slow confirm",
            expectation = "Confirm stays busy until the suspending handler returns; input is refused.",
        ) {
            DialogExample(
                trigger = "Open slow dialog",
                title = "Archive project?",
                body = "Archiving takes a moment while we pack the files.",
                confirm = "Archive",
                decline = "Cancel",
                danger = false,
                confirmDelayMs = 2000,
            )
        }
        case(
            name = "Long body text",
            expectation = "Body wraps and the dialog grows; the buttons stay pinned at the bottom.",
        ) {
            DialogExample(
                trigger = "Open long dialog",
                title = "Transfer ownership?",
                body = "The new owner gains full control of billing, members and integrations. " +
                    "You will keep admin access until they remove it, and this change is " +
                    "recorded in the audit log. Transfers cannot be reversed without support.",
                confirm = "Transfer",
                decline = "Cancel",
                danger = false,
            )
        }
    }
}

@Composable
private fun DialogExample(
    trigger: String,
    title: String,
    body: String,
    confirm: String,
    decline: String?,
    danger: Boolean,
    confirmDelayMs: Long = 0,
) {
    var open by remember { mutableStateOf(false) }

    Button(
        style = ButtonStyle.solid(if (danger) ActionColor.Danger else ActionColor.Brand),
        size = ButtonSize.Small,
        onClick = { open = true },
    ) {
        Text(trigger)
    }

    if (open) {
        ConfirmationDialog(
            title = { Text(title) },
            text = { Text(body) },
            confirmText = { Text(confirm) },
            onConfirm = {
                if (confirmDelayMs > 0) delay(confirmDelayMs)
                open = false
            },
            declineText = decline?.let { { Text(it) } },
            onDecline = decline?.let { { open = false } },
            leading = if (danger) {
                {
                    FeaturedIcon(
                        imageVector = ArecaIcons.AlertTriangle,
                        contentDescription = null,
                        style = FeaturedIconStyle.soft(ActionColor.Danger),
                    )
                }
            } else {
                null
            },
        )
    }
}
