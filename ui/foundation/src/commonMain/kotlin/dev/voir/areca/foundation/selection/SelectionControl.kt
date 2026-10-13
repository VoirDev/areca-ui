package dev.voir.areca.foundation.selection

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import dev.voir.areca.modifier.clickCursor

/**
 * Whether the bare selection controls in scope draw their state only.
 *
 * Provided as `true` by a row that is itself the toggle or the selection — a list row, a settings
 * row — around the control it draws. A [Checkbox], [Radio] or [Toggle] under it ignores its handler
 * and behaves as one given `null`: no click, no focus, no semantics of its own, no reserved touch
 * band. The row reports the state once, and a press on the control reaches the row like a press
 * anywhere else.
 */
internal val LocalSelectionControlInert = staticCompositionLocalOf { false }

/**
 * Makes a two- or three-state control answer a press, or describes it when it cannot.
 *
 * Three outcomes, by what the control is allowed to do:
 * - **Display-only** ([onClick] is `null`): nothing is added. The control is a picture of a state
 *   that an enclosing element reports.
 * - **Read-only** (and available): the role and the state are reported, with no action. Assistive
 *   technology reads the value and offers nothing to do with it, which is what read-only means.
 * - **Otherwise**: a `triStateToggleable` target with the Areca indication and a click cursor. An
 *   unavailable control is still a toggleable, disabled, so the platform announces it as such.
 *
 * Every word assistive technology says comes from the platform's own handling of [state] and
 * [role], so the control reads in the user's language without strings of its own.
 *
 * @param state State to report.
 * @param role Role to report: [Role.Checkbox] or [Role.Switch].
 * @param onClick Called on activation, or `null` for a display-only control.
 * @param enabled Whether the control is available. Wins over [readOnly].
 * @param readOnly Whether the control shows its state but rejects changes.
 * @param interactionSource Source the press, hover and focus interactions go to.
 * @param indication Indication drawn for those interactions.
 * @return The receiver with the control's input and semantics appended.
 */
internal fun Modifier.toggleControl(
    state: ToggleableState,
    role: Role,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    readOnly: Boolean,
    interactionSource: MutableInteractionSource,
    indication: Indication,
): Modifier = when {
    onClick == null -> this
    enabled && readOnly -> semantics {
        this.role = role
        toggleableState = state
    }

    else -> triStateToggleable(
        state = state,
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        role = role,
        onClick = onClick,
    ).clickCursor(enabled)
}

/**
 * Makes one option of a single choice answer a press, or describes it when it cannot.
 *
 * The [toggleControl] rules, for a control that reports `selected` rather than a toggleable state.
 *
 * @param selected Whether this option is the chosen one.
 * @param role Role to report, normally [Role.RadioButton].
 * @param onClick Called on activation, or `null` for a display-only control.
 * @param enabled Whether the control is available. Wins over [readOnly].
 * @param readOnly Whether the control shows its state but rejects changes.
 * @param interactionSource Source the press, hover and focus interactions go to.
 * @param indication Indication drawn for those interactions.
 * @return The receiver with the control's input and semantics appended.
 */
internal fun Modifier.selectControl(
    selected: Boolean,
    role: Role,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    readOnly: Boolean,
    interactionSource: MutableInteractionSource,
    indication: Indication,
): Modifier = when {
    onClick == null -> this
    enabled && readOnly -> semantics {
        this.role = role
        this.selected = selected
    }

    else -> selectable(
        selected = selected,
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        role = role,
        onClick = onClick,
    ).clickCursor(enabled)
}
