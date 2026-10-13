package dev.voir.areca.flow.profile.sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.LogOut
import dev.voir.areca.flow.profile.icon.Trash
import dev.voir.areca.flow.profile.rememberLastNonNull
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.FieldLabelPlacement
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Asks before signing out of this device.
 *
 * @param visible Whether the sheet is shown.
 * @param onConfirm Called when the person confirms.
 * @param onDismiss Called when the sheet is dismissed any other way.
 * @param confirming Whether signing out is in flight.
 * @param note What signing out costs on this device — unsynchronised changes, say — or `null` for
 *   no note. Drawn in the warning colour, since it is only worth saying when something is at stake.
 * @param title The question.
 * @param description What signing out does, or `null` for none.
 * @param confirmText Caption of the confirming button.
 * @param cancelText Caption of the cancelling button.
 */
@Composable
public fun SignOutSheet(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirming: Boolean = false,
    note: String? = null,
    title: String = "Sign out?",
    description: String? = null,
    confirmText: String = "Sign out",
    cancelText: String = "Cancel",
) {
    ConfirmSheet(
        visible = visible,
        icon = BundledIcons.LogOut,
        title = title,
        description = description,
        confirmText = confirmText,
        note = note,
        noteColor = ArecaTheme.colors.fgWarnText,
        confirming = confirming,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        cancelText = cancelText,
    )
}

/**
 * Asks before ending every session but this one.
 *
 * Its note is the reassurance rather than the warning: the thing people worry about when ending
 * other sessions is being signed out themselves.
 *
 * @param visible Whether the sheet is shown.
 * @param onConfirm Called when the person confirms.
 * @param onDismiss Called when the sheet is dismissed any other way.
 * @param confirming Whether ending the sessions is in flight.
 * @param title The question.
 * @param description What ending the sessions does, or `null` for none.
 * @param note The reassurance, or `null` for none.
 * @param confirmText Caption of the confirming button.
 * @param cancelText Caption of the cancelling button.
 */
@Composable
public fun SignOutOthersSheet(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirming: Boolean = false,
    title: String = "Sign out all other devices?",
    description: String? = null,
    note: String? = null,
    confirmText: String = "Sign out other devices",
    cancelText: String = "Cancel",
) {
    ConfirmSheet(
        visible = visible,
        icon = BundledIcons.LogOut,
        title = title,
        description = description,
        confirmText = confirmText,
        note = note,
        confirming = confirming,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        cancelText = cancelText,
    )
}

/**
 * Asks before ending one session — the sheet a pressed `SessionRow` opens.
 *
 * Names the device and what it runs in the description, so the person confirms the device they
 * meant rather than the row they happened to press. The sheet is up while [session] is set; the last
 * session is held while it closes, so its name does not vanish halfway through the animation. Only
 * the session on screen is ever confirmed, and only while [session] is set: a press during the close
 * is ignored rather than ending the session that is being drawn on its way out.
 *
 * @param session The session to end, or `null` when the sheet is down.
 * @param onConfirm Called with the session the sheet shows — [session] — when the person confirms.
 * @param onDismiss Called when the sheet is dismissed any other way.
 * @param confirming Whether ending the session is in flight.
 * @param title The question.
 * @param note What ending the session means, or `null` for none.
 * @param confirmText Caption of the confirming button.
 * @param cancelText Caption of the cancelling button.
 * @param description Writes the line under the question that names the session, or returns `null`
 *   for none. Receives the session shown; composable, so it can read the application's string
 *   resources. Defaults to the session's name, client and when it was last seen, joined by " · ".
 */
@Composable
public fun RevokeSessionSheet(
    session: SessionItem?,
    onConfirm: (SessionItem) -> Unit,
    onDismiss: () -> Unit,
    confirming: Boolean = false,
    title: String = "Sign out this device?",
    note: String? = null,
    confirmText: String = "Sign out device",
    cancelText: String = "Cancel",
    description: @Composable (session: SessionItem) -> String? = { item ->
        listOfNotNull(item.name, item.client, item.seen).joinToString(" · ")
    },
) {
    val target = rememberLastNonNull(session) ?: return

    ConfirmSheet(
        visible = session != null,
        icon = BundledIcons.LogOut,
        title = title,
        description = description(target),
        confirmText = confirmText,
        note = note,
        confirming = confirming,
        // `target` is what the sheet draws. While closing it is a session the caller has let go of,
        // so a press then confirms nothing.
        onConfirm = { if (session != null) onConfirm(target) },
        onDismiss = onDismiss,
        cancelText = cancelText,
    )
}

/**
 * Asks before deleting the account, optionally behind a typed phrase.
 *
 * With a [confirmationPhrase], a box under the note must hold that phrase before the button
 * enables. It is the only friction worth adding to a decision this size — a second sheet asking
 * "are you really sure?" is dismissed by the same reflex as the first, and typing a word is not.
 * The box is emptied every time the sheet opens, so an earlier half-typed phrase never carries over.
 * The instruction above it is the box's label to assistive technology, so the box is announced as
 * what to type rather than only by its placeholder.
 *
 * Whether deletion is immediate, scheduled or reversible is the application's to say, in
 * [description]; the module does not know.
 *
 * @param visible Whether the sheet is shown.
 * @param onConfirm Called when the person confirms.
 * @param onDismiss Called when the sheet is dismissed any other way.
 * @param confirming Whether deletion is in flight.
 * @param confirmationPhrase What must be typed to confirm — matched ignoring case and surrounding
 *   spaces — or `null` for a plain confirmation.
 * @param description What deleting removes — and whether it can be undone — or `null` for none.
 * @param title The question.
 * @param note The warning, or `null` for none — a product whose deletion can be undone says so in
 *   [description] and drops it.
 * @param confirmText Caption of the confirming button.
 * @param cancelText Caption of the cancelling button.
 * @param confirmationPrompt Instruction above the box when [confirmationPhrase] is set. Receives the
 *   phrase; composable, so it can read the application's string resources.
 */
@Composable
public fun DeleteAccountSheet(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirming: Boolean = false,
    confirmationPhrase: String? = null,
    description: String? = null,
    title: String = "Delete this account?",
    note: String? = null,
    confirmText: String = "Delete account",
    cancelText: String = "Cancel",
    confirmationPrompt: @Composable (phrase: String) -> String = { "Type $it to confirm." },
) {
    var typed by remember { mutableStateOf("") }

    LaunchedEffect(visible) {
        if (visible) typed = ""
    }

    val matches = confirmationPhrase == null || confirmationMatches(typed, confirmationPhrase)

    ConfirmSheet(
        visible = visible,
        icon = BundledIcons.Trash,
        title = title,
        description = description,
        confirmText = confirmText,
        note = note,
        noteColor = ArecaTheme.colors.fgDangerText,
        confirming = confirming,
        confirmEnabled = matches,
        onConfirm = { if (matches) onConfirm() },
        onDismiss = onDismiss,
        cancelText = cancelText,
        extra = confirmationPhrase?.let { phrase ->
            {
                val prompt = confirmationPrompt(phrase)

                Text(
                    text = prompt,
                    // Read as the box's label instead, where it says what the box is for.
                    modifier = Modifier.fillMaxWidth().clearAndSetSemantics {},
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgSubtle,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(ArecaTheme.spacing.md))

                // The label is drawn above, centred, as the sheet's instruction; the group only
                // names the box with it.
                FieldGroup(
                    modifier = Modifier.fillMaxWidth(),
                    label = prompt,
                    labelPlacement = FieldLabelPlacement.None,
                    enabled = !confirming,
                ) { field ->
                    TextField(
                        value = typed,
                        onValueChange = { typed = it },
                        modifier = Modifier.fillMaxWidth(),
                        field = field,
                        singleLine = true,
                        placeholder = { Text(phrase) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = if (phrase == phrase.uppercase()) {
                                KeyboardCapitalization.Characters
                            } else {
                                KeyboardCapitalization.None
                            },
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Done,
                        ),
                    )
                }
            }
        },
    )
}

/**
 * Whether what was typed confirms a deletion.
 *
 * Case and surrounding whitespace are ignored: the phrase is a deliberate act, not a password, and
 * refusing "delete" for "DELETE" punishes the person for their keyboard's capitalisation.
 *
 * @param typed What is in the box.
 * @param phrase What was asked for.
 * @return `true` when [typed] is [phrase].
 */
internal fun confirmationMatches(typed: String, phrase: String): Boolean =
    typed.trim().equals(phrase.trim(), ignoreCase = true)
