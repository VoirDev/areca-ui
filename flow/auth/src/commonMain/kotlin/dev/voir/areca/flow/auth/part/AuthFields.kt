package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.isSensitiveData
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.OtpField
import dev.voir.areca.foundation.field.PasswordField
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.text.Text

/*
 * The credential fields, one composable each.
 *
 * A form is four decisions — which boxes, in what order, with what between them — and every
 * product answers them differently. So the module does not ship a form. It ships the boxes, each
 * already carrying the three things applications reliably get wrong:
 *
 *   - the autofill content type, without which no password manager offers anything — the
 *     `contentType` semantics Android reads, and the `UITextContentType` iOS reads from the keyboard
 *     options (see [AuthTextKind]);
 *   - a keyboard configured for what is being typed, and an IME action that goes somewhere: with no
 *     callback the platform's own action runs, so Next moves focus on and Done closes the keyboard;
 *   - a label, placeholder and error sentence the application words, passed in where it composes
 *     the box — the label has an English default, the rest are drawn only when given.
 *
 * Each takes a [FocusRequester] and an IME callback so a caller can chain them in whatever order
 * its form has. The chaining is the caller's, because only the caller knows what comes next. The
 * code box usually needs neither: it is the only box on its screen, and it can submit itself when
 * full.
 *
 * All four share one parameter order — value, change callback, modifier, then error, newAccount
 * where there is one, focusRequester, imeAction, onImeAction, label and placeholder, then each
 * box's own extras, and enabled last.
 */

/**
 * The name box.
 *
 * @param value Name as typed.
 * @param onValueChange Called on every edit.
 * @param modifier Modifier applied to the field.
 * @param error Complaint to show beneath it, already worded, or `null` when it has nothing to say.
 * @param focusRequester Handle a previous field can use to move focus here.
 * @param imeAction Action the keyboard offers. [ImeAction.Next] when another box follows.
 * @param onImeAction Called when the keyboard's action is pressed, in place of the platform's own,
 *   or `null` to keep the platform's: focus moves on for [ImeAction.Next] and the keyboard closes
 *   for [ImeAction.Done].
 * @param label Label above the box.
 * @param placeholder Shown in the box while it is empty, or `null` for none.
 * @param enabled Whether the field accepts input.
 */
@Composable
public fun AuthNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    label: String = "Full name",
    placeholder: String? = null,
    enabled: Boolean = true,
) {
    val onHero = LocalAuthOnHero.current

    FieldGroup(
        modifier = modifier,
        label = label,
        error = error,
        style = AuthOnHero.fieldStyle(onHero),
    ) { field ->
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequesterOrNull(focusRequester)
                .authTextSemantics(AuthTextKind.Name),
            field = field,
            enabled = enabled,
            singleLine = true,
            placeholder = placeholder?.let { text -> { Text(text) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = imeAction,
                platformImeOptions = AuthTextKind.Name.platformImeOptions(),
            ),
            keyboardActions = imeActions(onImeAction),
        )
    }
}

/**
 * The email box.
 *
 * The keyboard is the email keyboard with autocorrection and automatic capitalisation both off, so
 * an address is neither "corrected" into a dictionary word nor given a capital first letter.
 *
 * @param value Email as typed.
 * @param onValueChange Called on every edit.
 * @param modifier Modifier applied to the field.
 * @param error Complaint to show beneath it, already worded, or `null` when it has nothing to say.
 * @param newAccount Whether this box is creating an account rather than signing in to one. It
 *   changes the Android autofill content type to a new username that is also an email address,
 *   which is what decides whether a password manager offers a saved address or offers to save this
 *   one. On iOS both cases are the `username` content type.
 * @param focusRequester Handle a previous field can use to move focus here.
 * @param imeAction Action the keyboard offers.
 * @param onImeAction Called when the keyboard's action is pressed, in place of the platform's own,
 *   or `null` to keep the platform's: focus moves on for [ImeAction.Next] and the keyboard closes
 *   for [ImeAction.Done].
 * @param label Label above the box.
 * @param placeholder Shown in the box while it is empty — "name@example.com" — or `null` for none.
 * @param enabled Whether the field accepts input.
 */
@Composable
public fun AuthEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    newAccount: Boolean = false,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    label: String = "Email",
    placeholder: String? = null,
    enabled: Boolean = true,
) {
    val onHero = LocalAuthOnHero.current
    val kind = if (newAccount) AuthTextKind.NewEmail else AuthTextKind.Email

    FieldGroup(
        modifier = modifier,
        label = label,
        error = error,
        style = AuthOnHero.fieldStyle(onHero),
    ) { field ->
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequesterOrNull(focusRequester)
                .authTextSemantics(kind),
            field = field,
            enabled = enabled,
            singleLine = true,
            placeholder = placeholder?.let { text -> { Text(text) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = imeAction,
                platformImeOptions = kind.platformImeOptions(),
            ),
            keyboardActions = imeActions(onImeAction),
        )
    }
}

/**
 * The password box.
 *
 * What is typed is marked as sensitive data, so accessibility services that are not assistive tools
 * cannot read it.
 *
 * @param value Password as typed.
 * @param onValueChange Called on every edit.
 * @param modifier Modifier applied to the field.
 * @param error Complaint to show beneath it, already worded, or `null` when it has nothing to say.
 * @param newAccount Whether a password is being chosen rather than recalled. It changes the
 *   autofill content type on Android and iOS: `NewPassword` is what makes a password manager offer
 *   to generate one instead of filling the saved one.
 * @param focusRequester Handle a previous field can use to move focus here.
 * @param imeAction Action the keyboard offers. [ImeAction.Done] on the last box of a form.
 * @param onImeAction Called when the keyboard's action is pressed, in place of the platform's own,
 *   or `null` to keep the platform's: focus moves on for [ImeAction.Next] and the keyboard closes
 *   for [ImeAction.Done].
 * @param label Label above the box. Pass the confirmation label for the second box of a pair.
 * @param placeholder Shown in the box while it is empty, or `null` for none.
 * @param revealDescription Accessibility label of the control that reveals the password.
 * @param hideDescription Accessibility label of the control that hides it again.
 * @param enabled Whether the field accepts input.
 */
@Composable
public fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    newAccount: Boolean = false,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    label: String = "Password",
    placeholder: String? = null,
    revealDescription: String = "Show password",
    hideDescription: String = "Hide password",
    enabled: Boolean = true,
) {
    val onHero = LocalAuthOnHero.current
    val kind = if (newAccount) AuthTextKind.NewPassword else AuthTextKind.Password

    FieldGroup(
        modifier = modifier,
        label = label,
        error = error,
        style = AuthOnHero.fieldStyle(onHero),
    ) { field ->
        PasswordField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequesterOrNull(focusRequester)
                .authTextSemantics(kind),
            field = field,
            enabled = enabled,
            placeholder = placeholder?.let { text -> { Text(text) } },
            revealContentDescription = revealDescription,
            hideContentDescription = hideDescription,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction,
                platformImeOptions = kind.platformImeOptions(),
            ),
            keyboardActions = imeActions(onImeAction),
        )
    }
}

/**
 * The box for a code that was sent to the person — by email, by text message, or by an
 * authenticator.
 *
 * A foundation `OtpField`, one character to a cell, with what an authentication screen needs on
 * top: the one-time-code autofill type on Android and iOS, so a keyboard offers the code it just
 * read from a message and a password manager fills one it generated; the code marked as sensitive
 * data; a label a tap on focuses the cells through; and the complaint under the cells.
 *
 * [onComplete] fires when the last cell is filled, however it was filled. Verifying from there is
 * what makes a code screen feel finished; the button under it stays for anyone who wants to press
 * something, and for the case where verifying failed and the same code is tried again.
 *
 * @param value Code as typed so far. Only digits are accepted.
 * @param onValueChange Called on every edit, with the code already filtered and cut to [length].
 * @param modifier Modifier applied to the field.
 * @param error Complaint to show beneath the cells, already worded — the code is wrong, it has
 *   expired — or `null` when there is nothing to say.
 * @param focusRequester Handle a screen can use to put focus in the cells on arrival.
 * @param imeAction Action the keyboard offers, on keyboards that offer one beside the digits.
 * @param onImeAction Called when the keyboard's action is pressed, in place of the platform's own,
 *   or `null` to keep the platform's: focus moves on for [ImeAction.Next] and the keyboard closes
 *   for [ImeAction.Done]. For a screen that does not verify on [onComplete] and
 *   wants the keyboard's action to submit.
 * @param label Label above the cells.
 * @param assistive Hint under the cells while there is no [error] — "It expires in 10 minutes" —
 *   or `null` for none.
 * @param length Number of cells — the length of the codes the service sends.
 * @param groupSize Cells per group, or `null` for one run. Match how the message prints the code.
 * @param onComplete Called with the code when an edit fills the last cell, or `null` to wait for
 *   the button.
 * @param enabled Whether the cells accept input. Pass `false` while the code is being verified.
 */
@Composable
public fun AuthCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    focusRequester: FocusRequester? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    label: String = "Verification code",
    assistive: String? = null,
    length: Int = 6,
    groupSize: Int? = null,
    onComplete: ((String) -> Unit)? = null,
    enabled: Boolean = true,
) {
    val onHero = LocalAuthOnHero.current

    FieldGroup(
        modifier = modifier,
        label = label,
        assistive = assistive,
        error = error,
        style = AuthOnHero.fieldStyle(onHero),
    ) { field ->
        OtpField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequesterOrNull(focusRequester)
                .authTextSemantics(AuthTextKind.OneTimeCode),
            length = length,
            onComplete = onComplete,
            field = field,
            enabled = enabled,
            stretch = true,
            groupSize = groupSize,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction,
                platformImeOptions = AuthTextKind.OneTimeCode.platformImeOptions(),
            ),
            keyboardActions = imeActions(onImeAction),
        )
    }
}

/**
 * Applies a focus requester when there is one.
 *
 * A field that nobody moves focus to should not carry a requester, and a caller that chains two
 * fields should not have to construct one for the first.
 *
 * @param focusRequester Handle to attach, or `null` to attach none.
 * @return This modifier, with the requester attached when there was one.
 */
private fun Modifier.focusRequesterOrNull(focusRequester: FocusRequester?): Modifier =
    if (focusRequester == null) this else focusRequester(focusRequester)

/**
 * Marks a box with what it holds, for Android autofill and for accessibility services.
 *
 * @param kind What the box holds.
 * @return This modifier, with the content type and, for a secret, the sensitive-data flag.
 */
private fun Modifier.authTextSemantics(kind: AuthTextKind): Modifier = semantics {
    contentType = kind.contentType
    if (kind.isSensitive) isSensitiveData = true
}

/**
 * Routes every keyboard action to one callback, or leaves them all to the platform.
 *
 * Which action the keyboard offers is already decided by the field's `imeAction`, so the caller
 * has one thing to handle rather than six identical branches. Without a callback the platform's
 * default runs — focus moves on for Next, the keyboard closes for Done — which is why a box nobody
 * chained still behaves.
 *
 * @param onImeAction Called in place of the platform's action, or `null` to keep the platform's.
 * @return Actions to pass to the field.
 */
internal fun imeActions(onImeAction: (() -> Unit)?): KeyboardActions =
    if (onImeAction == null) {
        KeyboardActions.Default
    } else {
        KeyboardActions(
            onDone = { onImeAction() },
            onGo = { onImeAction() },
            onNext = { onImeAction() },
            onPrevious = { onImeAction() },
            onSearch = { onImeAction() },
            onSend = { onImeAction() },
        )
    }
