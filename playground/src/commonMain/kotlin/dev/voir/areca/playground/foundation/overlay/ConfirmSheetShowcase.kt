package dev.voir.areca.playground.foundation.overlay

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.Archive
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.LogOut
import dev.voir.areca.icons.Trash
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay

/** The word the typed-confirmation examples ask for before the confirming button accepts a press. */
private const val ConfirmWord = "DELETE"

/** How long the examples' confirmed work takes, in milliseconds, so the in-flight state is seen. */
private const val WorkMillis = 1500L

/** How long the in-flight state cell holds the sheet busy before it closes, in milliseconds. */
private const val HeldConfirmingMillis = 4000L

/** The note colours a confirmation sheet is given in practice. */
private enum class NoteTone {
    /** `fgSubtle`: a consequence worth saying, but not a warning. The default. */
    Subtle,

    /** `fgWarnText`: something is lost, but it can be got back. */
    Warn,

    /** `fgDangerText`: something is lost for good. */
    Danger,
    ;

    /**
     * Resolves the tone from the current theme.
     *
     * @return The colour to pass as `noteColor`.
     */
    @Composable
    fun color(): Color = when (this) {
        Subtle -> ArecaTheme.colors.fgSubtle
        Warn -> ArecaTheme.colors.fgWarnText
        Danger -> ArecaTheme.colors.fgDangerText
    }
}

/**
 * Showcase for [ConfirmSheet].
 *
 * The sheet is an overlay, so every cell carries a button that raises it; the rest of the cell says
 * whether it is up.
 */
@Composable
fun ConfirmSheetShowcase() = ShowcaseScaffold(
    title = "Confirm sheet",
    summary = "Bottom sheet that asks once before an action that is hard to take back.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Open the sheet, then change the controls and open it again.") {
        val title by textControl("title", default = "Delete this workspace?")
        val description by textControl(
            key = "description",
            default = "Every project, file and comment in Northwind Studio is removed for all members.",
        )
        val confirmText by textControl("confirmText", default = "Delete workspace")
        val cancelText by textControl("cancelText", default = "Cancel")
        val withIcon by booleanControl(
            key = "icon",
            default = true,
            description = "A neutral tile at the top; off for a question that needs no mark.",
        )
        val withNote by booleanControl("note", default = true)
        val noteTone by enumControl(
            key = "noteColor",
            default = NoteTone.Danger,
            options = NoteTone.entries,
            description = "The sheet's only status colour.",
        )
        val slowConfirm by booleanControl(
            key = "confirming",
            default = true,
            label = "confirming on confirm",
            description = "Confirm starts work that takes a moment; the sheet is in flight until it ends.",
        )
        val confirmEnabled by booleanControl("confirmEnabled", default = true)
        val withExtra by booleanControl(
            key = "extra",
            default = false,
            description = "A field between the note and the buttons; confirm waits for \"$ConfirmWord\".",
        )

        var open by remember { mutableStateOf(false) }
        var confirming by remember { mutableStateOf(false) }
        var typed by remember { mutableStateOf("") }

        actionControl(
            key = "open",
            buttonLabel = "Open sheet",
            onInvoke = {
                typed = ""
                open = true
            },
        )

        preview {
            // The confirmed work. The sheet refuses every dismissal while it runs, so the preview —
            // like a real caller — must clear `confirming` itself when the work ends.
            LaunchedEffect(confirming) {
                if (confirming) {
                    delay(WorkMillis)
                    confirming = false
                    open = false
                }
            }

            Text(
                text = if (open) "Sheet is open" else "Sheet is closed",
                variant = TextVariant.BodySmall,
                color = TextColor.Muted,
            )

            ConfirmSheet(
                visible = open,
                icon = if (withIcon) ArecaIcons.Trash else null,
                title = title,
                description = description.ifBlank { null },
                confirmText = confirmText,
                onConfirm = { if (slowConfirm) confirming = true else open = false },
                onDismiss = { open = false },
                note = if (withNote) "This can't be undone." else null,
                noteColor = noteTone.color(),
                confirming = confirming,
                confirmEnabled = confirmEnabled && (!withExtra || typed == ConfirmWord),
                extra = if (withExtra) {
                    {
                        TextField(
                            value = typed,
                            onValueChange = { typed = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("Type $ConfirmWord to confirm") },
                        )
                    }
                } else {
                    null
                },
                cancelText = cancelText,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Default", detail = "both buttons answer") {
            SheetExample(
                trigger = "Sign out",
                icon = ArecaIcons.LogOut,
                title = "Sign out of Areca?",
                description = "You can sign back in at any time with your email and password.",
                confirmText = "Sign out",
            )
        }
        variant("confirmEnabled = false", detail = "confirm waits for the field to be filled") {
            SheetExample(
                trigger = "Delete workspace",
                icon = ArecaIcons.Trash,
                title = "Delete this workspace?",
                description = "Every project, file and comment in Northwind Studio is removed.",
                confirmText = "Delete workspace",
                note = "This can't be undone.",
                noteTone = NoteTone.Danger,
                typeToConfirm = true,
            )
        }
        variant("confirming = true", detail = "opens in flight for 4s; nothing dismisses it") {
            SheetExample(
                trigger = "Archive project",
                icon = ArecaIcons.Archive,
                title = "Archive Q3 launch plan?",
                description = "It leaves the sidebar and becomes read-only for everyone.",
                confirmText = "Archive",
                opensConfirming = true,
            )
        }
    }

    Recipes {
        recipe(
            name = "Destructive, typed confirmation",
            description = "A danger note says what is lost; the field makes the answer deliberate.",
        ) {
            SheetExample(
                trigger = "Delete workspace",
                icon = ArecaIcons.Trash,
                title = "Delete this workspace?",
                description = "Every project, file and comment in Northwind Studio is removed for " +
                    "all 14 members.",
                confirmText = "Delete workspace",
                note = "This can't be undone.",
                noteTone = NoteTone.Danger,
                typeToConfirm = true,
            )
        }
        recipe(
            name = "Leaving a form",
            description = "No icon — discarding a draft is neither good nor bad news.",
        ) {
            SheetExample(
                trigger = "Close editor",
                icon = null,
                title = "Discard your changes?",
                description = "The edits you made to this invoice haven't been saved.",
                confirmText = "Discard",
                cancelText = "Keep editing",
            )
        }
        recipe(
            name = "Recoverable loss",
            description = "A warning note for something that can be got back.",
        ) {
            SheetExample(
                trigger = "Archive project",
                icon = ArecaIcons.Archive,
                title = "Archive Q3 launch plan?",
                description = "It leaves the sidebar and becomes read-only for everyone.",
                confirmText = "Archive",
                note = "You can restore it from Archived projects for 30 days.",
                noteTone = NoteTone.Warn,
            )
        }
        recipe(
            name = "Sign out",
            description = "A subtle note for a consequence that is worth saying but is not a warning.",
        ) {
            SheetExample(
                trigger = "Sign out",
                icon = ArecaIcons.LogOut,
                title = "Sign out of Areca?",
                description = null,
                confirmText = "Sign out",
                note = "Offline drafts on this device are kept until you sign in again.",
                noteTone = NoteTone.Subtle,
            )
        }
    }

    EdgeCases {
        case(
            name = "Title only",
            expectation = "No icon, description or note: the question sits straight above the buttons.",
        ) {
            SheetExample(
                trigger = "Leave call",
                icon = null,
                title = "Leave the call?",
                description = null,
                confirmText = "Leave",
            )
        }
        case(
            name = "Long copy",
            expectation = "Title and description wrap, centred; the sheet grows to fit its content.",
        ) {
            SheetExample(
                trigger = "Transfer ownership",
                icon = ArecaIcons.LogOut,
                title = "Transfer ownership of Northwind Studio to Dana Whitfield?",
                description = "Dana gains full control of billing, members and integrations. You " +
                    "keep admin access until she removes it, and the change is recorded in the " +
                    "audit log. Ownership cannot be taken back without contacting support.",
                confirmText = "Transfer ownership",
                note = "Your current plan and invoices move with the workspace.",
                noteTone = NoteTone.Warn,
            )
        }
    }
}

/**
 * A button that raises one [ConfirmSheet], and the sheet it raises.
 *
 * Confirming runs [WorkMillis] of pretend work, so the in-flight state of every example is seen
 * before the sheet closes.
 *
 * @param trigger Caption of the button that opens the sheet.
 * @param icon Glyph at the top of the sheet, or `null` for none.
 * @param title The question.
 * @param description What happens on a yes, or `null` for the question alone.
 * @param confirmText Caption of the confirming button.
 * @param note The tinted note, or `null` for none.
 * @param noteTone Colour of [note].
 * @param cancelText Caption of the cancelling button.
 * @param typeToConfirm Whether the sheet carries a field that must read [ConfirmWord] before the
 *   confirming button accepts a press.
 * @param opensConfirming Whether the sheet opens already in flight, and stays so for
 *   [HeldConfirmingMillis] before it closes — the state held still long enough to inspect.
 */
@Composable
private fun SheetExample(
    trigger: String,
    icon: ImageVector?,
    title: String,
    description: String?,
    confirmText: String,
    note: String? = null,
    noteTone: NoteTone = NoteTone.Subtle,
    cancelText: String = "Cancel",
    typeToConfirm: Boolean = false,
    opensConfirming: Boolean = false,
) {
    var open by remember { mutableStateOf(false) }
    var working by remember { mutableStateOf(false) }
    var typed by remember { mutableStateOf("") }

    // The confirmed work: in flight for a moment, then the caller closes the sheet, as a real screen
    // would once its request returns.
    LaunchedEffect(working) {
        if (working) {
            delay(if (opensConfirming) HeldConfirmingMillis else WorkMillis)
            working = false
            open = false
        }
    }

    Button(
        style = ButtonStyle.outlined(ActionColor.Neutral),
        size = ButtonSize.Small,
        onClick = {
            typed = ""
            open = true
            working = opensConfirming
        },
    ) {
        Text(trigger)
    }

    ConfirmSheet(
        visible = open,
        icon = icon,
        title = title,
        description = description,
        confirmText = confirmText,
        onConfirm = { working = true },
        onDismiss = { open = false },
        note = note,
        noteColor = noteTone.color(),
        confirming = working,
        confirmEnabled = !typeToConfirm || typed == ConfirmWord,
        extra = if (typeToConfirm) {
            {
                TextField(
                    value = typed,
                    onValueChange = { typed = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Type $ConfirmWord to confirm") },
                )
            }
        } else {
            null
        },
        cancelText = cancelText,
    )
}
