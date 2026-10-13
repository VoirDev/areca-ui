package dev.voir.areca.playground.foundation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.overlay.Dialog
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [Dialog]. */
@Composable
fun DialogShowcase() = ShowcaseScaffold(
    title = "Dialog",
    summary = "Centred modal surface with header, content and footer regions.",
    module = "foundation",
) {
    Playground(description = "Open the dialog and try each dismissal route.") {
        val withHeader by booleanControl("header", default = true)
        val withFooter by booleanControl("footer", default = true)
        val dismissOnOverlay by booleanControl(
            key = "dismissOnOverlayClick",
            default = true,
            description = "Whether a click on the scrim closes it.",
        )
        val dismissOnBack by booleanControl("dismissOnBackPress", default = true)

        var open by remember { mutableStateOf(false) }

        actionControl(key = "open", buttonLabel = "Open dialog", onInvoke = { open = true })

        preview {
            Text(
                text = if (open) "Dialog is open" else "Dialog is closed",
                variant = TextVariant.BodySmall,
                color = TextColor.Muted,
            )

            if (open) {
                Dialog(
                    onDismissRequest = { open = false },
                    dismissOnOverlayClick = dismissOnOverlay,
                    dismissOnBackPress = dismissOnBack,
                    dismissOnClickOutside = dismissOnOverlay,
                    header = if (withHeader) ({ DialogHeader("Invite members") }) else null,
                    footer = if (withFooter) {
                        { DialogFooter(onCancel = { open = false }, onConfirm = { open = false }) }
                    } else {
                        null
                    },
                ) {
                    DialogBody()
                }
            }
        }
    }

    Anatomy(description = "Three regions. Only the content is required.") {
        slot(name = "header", description = "Title and, usually, a close affordance.") {
            DialogExample(label = "Header only", header = true, footer = false)
        }
        slot(name = "content", description = "The dialog's body. Scrolls if it has to.") {
            DialogExample(label = "Content only", header = false, footer = false)
        }
        slot(name = "footer", description = "Actions, pinned below the content.") {
            DialogExample(label = "Footer only", header = false, footer = true)
        }
    }

    EdgeCases {
        case(
            name = "Undismissable",
            expectation = "Scrim clicks and back press do nothing; only the footer can close it.",
        ) {
            DialogExample(
                label = "Open undismissable",
                header = true,
                footer = true,
                dismissible = false,
            )
        }
        case(
            name = "Long content",
            expectation = "Content scrolls within the dialog; header and footer stay put.",
        ) {
            DialogExample(label = "Open long dialog", header = true, footer = true, bodyRows = 20)
        }
    }
}

@Composable
private fun DialogExample(
    label: String,
    header: Boolean,
    footer: Boolean,
    dismissible: Boolean = true,
    bodyRows: Int = 3,
) {
    var open by remember { mutableStateOf(false) }

    Button(
        style = ButtonStyle.solid(ActionColor.Brand),
        size = ButtonSize.Small,
        onClick = { open = true },
    ) {
        Text(label)
    }

    if (open) {
        Dialog(
            onDismissRequest = { if (dismissible) open = false },
            dismissOnOverlayClick = dismissible,
            dismissOnBackPress = dismissible,
            dismissOnClickOutside = dismissible,
            header = if (header) ({ DialogHeader("Invite members") }) else null,
            footer = if (footer) {
                { DialogFooter(onCancel = { open = false }, onConfirm = { open = false }) }
            } else {
                null
            },
        ) {
            DialogBody(rows = bodyRows)
        }
    }
}

/**
 * A dialog's header, as a modal [TopBar] so it matches every sheet's.
 *
 * @param title What the dialog is for.
 */
@Composable
private fun DialogHeader(title: String) {
    TopBar(
        title = title,
        modifier = Modifier.fillMaxWidth(),
        size = TopBarSize.Modal,
        style = TopBarStyle.Modal,
    )
}

@Composable
private fun DialogFooter(onCancel: () -> Unit, onConfirm: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Button(
            style = ButtonStyle.outlined(ActionColor.Neutral),
            onClick = onCancel,
            modifier = Modifier.weight(1f),
        ) {
            Text("Cancel")
        }
        Button(
            style = ButtonStyle.solid(ActionColor.Brand),
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
        ) {
            Text("Send invites")
        }
    }
}

@Composable
private fun DialogBody(rows: Int = 3) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        repeat(rows) { index ->
            Text(
                text = "teammate${index + 1}@example.com",
                variant = TextVariant.BodySmall,
                color = TextColor.Subtle,
            )
        }
    }
}
