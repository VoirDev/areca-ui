package dev.voir.areca.foundation.field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics

/**
 * The handle a [FieldGroup] uses to move focus into the control it wraps.
 *
 * Wraps a [FocusRequester] together with whether anything has claimed it. A group cannot know
 * whether its content forwarded the scope — a caller is free to put a checkbox in there and
 * ignore it — and a [FocusRequester] that was never attached throws when asked for focus. So
 * [request] checks first and does nothing rather than crashing a form because its label was
 * tapped.
 *
 * The control that claims the handle also reports through it whether it holds focus, which is how
 * a group draws its label in the focused color.
 */
@Stable
public class FieldFocus {
    /** The requester handed to whichever node owns the control's focus. */
    internal val requester = FocusRequester()

    /**
     * Whether a control has claimed [requester].
     *
     * A plain field rather than snapshot state: it is written from composition effects and read
     * only from a pointer callback, so nothing needs to recompose when it changes.
     */
    internal var attached: Boolean = false

    /**
     * Whether the control that claimed this handle, or anything inside it, holds focus.
     *
     * Snapshot state, unlike [attached]: the group reads it while composing, to draw its label in
     * the focused color, and has to recompose when it changes.
     */
    internal var focused: Boolean by mutableStateOf(false)

    /**
     * Moves focus to the control, if one claimed this handle.
     *
     * @return Whether focus was requested. False means nothing forwarded the scope.
     */
    public fun request(): Boolean {
        if (!attached) return false
        requester.requestFocus()
        return true
    }
}

/**
 * What a [FieldGroup] hands to the control it wraps.
 *
 * Five things travel here. The [status] and [enabled] are the ones every control already needed,
 * so that the label, the message and the container cannot disagree about whether something is
 * wrong or whether the control takes input. The [label] and
 * [labelPlacement] say what the label is and where it goes — the container draws it only for a
 * [FieldLabelPlacement.Internal] placement, but it carries the text either way because the
 * control announces it. [contentDescription] and [errorDescription] are what a screen reader
 * should hear. [focus] is how tapping the label reaches the control.
 *
 * Pass it straight through:
 *
 * ```kotlin
 * FieldGroup(
 *     label = "Email",
 *     error = emailError,
 *     labelPlacement = FieldLabelPlacement.Inside,
 * ) { field ->
 *     TextField(value = email, onValueChange = { email = it }, field = field)
 * }
 * ```
 *
 * Building one by hand is supported, for a control that wants this treatment without a group
 * around it.
 *
 * @property status Validation outcome the group resolved.
 * @property label Label describing the control, wherever it is drawn, or null when there is none.
 * @property labelPlacement Where [label] goes. [Field] draws it only when this is
 *   [FieldLabelPlacement.Internal]; otherwise the group drew it itself.
 * @property contentDescription What the control should announce itself as. Set only when the
 *   label is drawn outside the control — an internal label is already a descendant of the
 *   control's semantics node, and naming it twice would talk over the value.
 * @property errorDescription Problem to report through the platform's invalid-input semantics,
 *   or null when nothing is wrong.
 * @property focus Handle that moves focus into the control and reports whether the control holds
 *   it, or null for a group that does not offer it.
 * @property enabled Whether the control accepts input. Every field takes its own `enabled` from
 *   here unless it is given one explicitly.
 */
@Immutable
public data class FieldScope(
    public val status: FieldStatus = FieldStatus.Default,
    public val label: FieldLabel? = null,
    public val labelPlacement: FieldLabelPlacement? = null,
    public val contentDescription: CharSequence? = null,
    public val errorDescription: CharSequence? = null,
    public val focus: FieldFocus? = null,
    public val enabled: Boolean = true,
)

/**
 * The label this scope wants drawn inside the container, paired with where it goes.
 *
 * @return Label and placement, or null when the label belongs outside the container or there is
 *   none.
 */
internal fun FieldScope?.internalLabel(): Pair<FieldLabel, FieldLabelPlacement.Internal>? {
    val label = this?.label ?: return null
    val placement = labelPlacement as? FieldLabelPlacement.Internal ?: return null
    return label to placement
}

/**
 * Applies everything a [FieldScope] needs on the node that owns the control.
 *
 * That node is the one the platform focuses and announces: the editor for a text field, the
 * container for a field whose value is chosen elsewhere. Exactly one node per control should
 * call this — two would attach the same [FocusRequester] twice and make it ambiguous which one
 * a label tap reaches.
 *
 * @param field Scope to apply, or null to leave the node alone.
 * @param focusable Whether the node can take focus. A node that cannot — a disabled or read-only
 *   select — still carries the scope's name and error, but leaves [FieldScope.focus] unclaimed so a
 *   label tap reports that nothing took it rather than requesting focus nowhere.
 * @return The modifier with focus and semantics attached.
 */
@Composable
internal fun Modifier.fieldControl(field: FieldScope?, focusable: Boolean = true): Modifier {
    if (field == null) return this

    val focus = field.focus?.takeIf { focusable }
    if (focus != null) {
        DisposableEffect(focus) {
            focus.attached = true
            onDispose {
                focus.attached = false
                focus.focused = false
            }
        }
    }

    val description = field.contentDescription?.toString()
    val errorText = field.errorDescription?.toString()
    if (focus == null && description == null && errorText == null) return this

    return this
        .then(
            if (focus != null) {
                Modifier
                    .focusRequester(focus.requester)
                    .onFocusChanged { focus.focused = it.hasFocus }
            } else {
                Modifier
            },
        )
        .semantics {
            if (description != null) contentDescription = description
            if (errorText != null) error(errorText)
        }
}
