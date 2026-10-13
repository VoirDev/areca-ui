package dev.voir.areca.foundation.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.EyeHide
import dev.voir.areca.icon.EyeShow
import dev.voir.areca.theme.ArecaTheme

/**
 * [TextField] for a secret: the value is masked, and a control in the trailing slot reveals it.
 *
 * Reveal state is owned by the field and resets whenever it leaves composition. Nothing is
 * remembered across navigation, which is the behavior you want — a password left visible on a
 * screen somebody comes back to is a password shown to whoever is standing behind them.
 *
 * A disabled field masks its value and disables the reveal control, and comes back masked when it
 * is enabled again: its user could not hide a revealed secret, so it is never left showing one. A
 * read-only field can still be revealed, because reading the value is exactly what it allows.
 *
 * The reveal control is drawn from the trailing slot, so passing [trailing] replaces it. Do that
 * when the reveal belongs somewhere else — one toggle governing a password and its confirmation,
 * say — rather than to hide it: a masked field with no way to check what was typed is why people
 * paste passwords into a text editor first.
 *
 * @param value Raw secret in your state.
 * @param onValueChange Called with the new value on every edit.
 * @param modifier Modifier applied to the field container.
 * @param format Optional display-only beautifier. Applied before masking, so it only shows once
 *   the value is revealed.
 * @param style Color palette and container shape. See [FieldStyle].
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [FieldGroup]: the status to report, and a label to
 *   draw inside the container when the group placed it there. Forward it and an internal
 *   label placement takes effect; leave it out and the group's label has nowhere to go.
 * @param status Semantic state — Default, Error, Success, Warning. Comes from [field]
 *   unless it is given explicitly.
 * @param enabled When false, non-interactive and rendered as disabled, with the value masked.
 *   Comes from [field] unless it is given explicitly.
 * @param readOnly Visible/selectable but not editable. The value can still be revealed.
 * @param loading Shows a loading indicator in place of the reveal control.
 * @param placeholder Shown when the value is empty.
 * @param leading Optional leading slot pinned to the start edge.
 * @param trailing Replaces the built-in reveal control.
 * @param keyboardOptions Software keyboard configuration. Defaults to a password keyboard; keep
 *   [KeyboardType.Password] when overriding it, or the platform may offer autocorrect and
 *   suggestions over the secret.
 * @param keyboardActions IME action handlers. An action only fires when [keyboardOptions] asks
 *   the IME for that action — set `imeAction` to reach the next field or submit the form.
 * @param revealContentDescription Accessibility label for the control while the value is masked.
 * @param hideContentDescription Accessibility label for the control while the value is revealed.
 * @param cursorBrush Brush painting the caret.
 * @param interactionSource Source of the field's interactions; pass one to observe focus and
 *   presses from outside, or `null` to let the field create its own.
 */
@Composable
public fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    format: ((String) -> String)? = null,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    revealContentDescription: String = "Show password",
    hideContentDescription: String = "Hide password",
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null,
) {
    var revealed by remember { mutableStateOf(false) }
    // Masked while disabled straight away, and reset so re-enabling does not reveal it again.
    val showing = revealed && enabled
    LaunchedEffect(enabled) {
        if (!enabled) revealed = false
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        format = format,
        size = size,
        style = style,
        field = field,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        loading = loading,
        leading = leading,
        trailing = trailing ?: {
            IconButton(
                onClick = { revealed = !revealed },
                enabled = enabled,
            ) {
                Icon(
                    imageVector = if (showing) {
                        BundledIcons.EyeShow
                    } else {
                        BundledIcons.EyeHide
                    },
                    contentDescription = if (showing) {
                        hideContentDescription
                    } else {
                        revealContentDescription
                    },
                )
            }
        },
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (showing) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        cursorBrush = cursorBrush,
        interactionSource = interactionSource,
    )
}
