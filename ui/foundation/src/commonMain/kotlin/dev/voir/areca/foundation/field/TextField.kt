package dev.voir.areca.foundation.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.voir.areca.theme.ArecaTheme

/**
 * Areca themed [TextField] — a styled wrapper around [BasicTextField] that renders a [Field]
 * decoration (container, border, leading/trailing slots, placeholder).
 *
 * `format` is a **display-only** beautifier: your state keeps the raw value, the field shows
 * the formatted one. The caret is remapped via [OffsetMapping] so it never jumps. Use your own
 * `onValueChange` for input filtering (e.g. digits-only, length caps).
 *
 * Selection is kept internally so it survives recomposition, but the hoisted [value] stays the
 * owner: whenever it disagrees with what was typed — a filter rejected a character, a view model
 * normalized the text — the selection collapses to the end of the new value. If you need a filter
 * that does not move the caret, filter into a [TextFieldValue] and use the overload below.
 *
 * @param value Raw text in your state (e.g. "1234567890").
 * @param onValueChange Called with the raw text the user produced (before formatting).
 * @param modifier Modifier applied to the field container.
 * @param format Optional pure function that beautifies the raw value for display
 *               (e.g. `"1234567890"` → `"(123) 456-7890"`). Does **not** alter state.
 * @param style Color palette and container shape. See [FieldStyle].
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [FieldGroup]: the status to report, and a label to
 *   draw inside the container when the group placed it there. Forward it and an internal
 *   label placement takes effect; leave it out and the group's label has nowhere to go.
 * @param status Semantic state — Default, Error, Success, Warning. Comes from [field]
 *   unless it is given explicitly.
 * @param enabled When false, non-interactive and rendered as disabled. Comes from
 *   [field] unless it is given explicitly.
 * @param readOnly Visible/selectable but not editable.
 * @param loading Shows a loading indicator (handled inside [Field]).
 * @param leading Optional leading slot pinned to the start edge.
 * @param trailing Optional trailing slot pinned to the end edge.
 * @param placeholder Shown when the value is empty.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardActions IME action handlers.
 * @param visualTransformation Extra display transform (e.g. password masking). If [format] is
 *                             provided, it is applied first; this transform runs on the result.
 * @param singleLine One-line field; Enter is consumed by IME action.
 * @param maxLines Upper line bound (defaults to 1 when [singleLine], else MAX_VALUE).
 * @param minLines Lower line bound.
 * @param cursorBrush Brush painting the caret.
 * @param interactionSource Hoisted interaction source. Pass one to observe focus and presses;
 *   `null` keeps one internally.
 */
@Composable
public fun TextField(
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
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null,
) {
    // Internal TFV preserves selection across recompositions. State owner is still `value`.
    var tfvState by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
    val current = if (tfvState.text == value) tfvState
    else tfvState.copy(text = value, selection = TextRange(value.length), composition = null)

    val effectiveVT = remember(format, visualTransformation) {
        when {
            format == null -> visualTransformation
            visualTransformation === VisualTransformation.None -> FormatVisualTransformation(format)
            else -> FormatVisualTransformation(format).then(visualTransformation)
        }
    }

    TextField(
        value = current,
        onValueChange = { new ->
            tfvState = new
            if (new.text != value) onValueChange(new.text)
        },
        modifier = modifier,
        style = style,
        size = size,
        field = field,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        loading = loading,
        leading = leading,
        trailing = trailing,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = effectiveVT,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = cursorBrush,
        interactionSource = interactionSource,
    )
}

/**
 * [TextFieldValue] overload — use when you need direct control over selection and composition.
 *
 * Unlike the [String] overload this one holds no internal state, so a caller who rewrites the
 * value in `onValueChange` decides for itself where the caret lands.
 *
 * @param value Current text together with its selection and composition.
 * @param onValueChange Called with the new value on every edit, including selection-only changes.
 * @param modifier Modifier applied to the field container.
 * @param style Color palette and container shape. See [FieldStyle].
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [FieldGroup]: the status to report, and a label to
 *   draw inside the container when the group placed it there. Forward it and an internal
 *   label placement takes effect; leave it out and the group's label has nowhere to go.
 * @param status Semantic state — Default, Error, Success, Warning. Comes from [field]
 *   unless it is given explicitly.
 * @param enabled When false, non-interactive and rendered as disabled. Comes from
 *   [field] unless it is given explicitly.
 * @param readOnly Visible/selectable but not editable.
 * @param loading Shows a loading indicator (handled inside [Field]).
 * @param leading Optional leading slot pinned to the start edge.
 * @param trailing Optional trailing slot pinned to the end edge. Hidden while [loading].
 * @param placeholder Shown when the value is empty.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardActions IME action handlers. An action only fires when
 *   [keyboardOptions] asks the IME for that action.
 * @param visualTransformation Display transform, e.g. password masking.
 * @param singleLine One-line field; Enter is consumed by the IME action.
 * @param maxLines Upper line bound (defaults to 1 when [singleLine], else MAX_VALUE).
 * @param minLines Lower line bound.
 * @param cursorBrush Brush painting the caret.
 * @param interactionSource Hoisted interaction source. The container watches it for focus, so
 *   pass the same instance if you observe focus yourself; `null` keeps one internally.
 */
@Composable
public fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val contentColor by animateColorAsState(
        targetValue = resolveContentColor(
            style = style,
            state = fieldVisualState(status, enabled, readOnly, focused),
        ),
        animationSpec = fieldColorSpec(),
        label = "fieldContent",
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fieldControl(field),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = size.textStyle.merge(color = contentColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        cursorBrush = cursorBrush,
        interactionSource = interactions,
        decorationBox = { innerTextField ->
            Field(
                style = style,
                size = size,
                field = field,
                status = status,
                enabled = enabled,
                readOnly = readOnly,
                loading = loading,
                empty = value.text.isEmpty(),
                leading = leading,
                trailing = trailing,
                placeholder = placeholder,
                interactionSource = interactions,
            ) {
                innerTextField()
            }
        },
    )
}

// region format -> VisualTransformation ------------------------------------------------

/**
 * Builds a display-only [VisualTransformation] from a `(String) -> String` beautifier.
 *
 * Assumes the formatter never strips characters — only inserts separators / decorations. If you
 * need to strip, do it in your `onValueChange` (input filter); format is for display. A formatter
 * that removes characters will produce an offset mapping that does not round-trip, and the caret
 * will land in the wrong place.
 *
 * Equality is by formatter identity, so pass a stable lambda — one hoisted into a `remember` or a
 * top-level function reference — or every recomposition looks like a new transformation.
 *
 * @property format Pure beautifier applied to the raw text before it is drawn.
 */
internal class FormatVisualTransformation(
    private val format: (String) -> String,
) : VisualTransformation {

    /**
     * Formats [text] for display and maps offsets between the two strings.
     *
     * @param text Raw text held in state.
     * @return Formatted text with a mapping back to [text]; the identity mapping when the
     *   formatter made no change.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        val transformed = format(original)
        if (transformed == original) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        return TransformedText(
            text = AnnotatedString(transformed),
            offsetMapping = FormatOffsetMapping(original, transformed),
        )
    }

    /**
     * Two transformations are equal when they wrap the same formatter instance.
     *
     * @param other Value to compare against.
     * @return Whether both wrap the identical formatter.
     */
    override fun equals(other: Any?): Boolean =
        this === other || (other is FormatVisualTransformation && other.format === format)

    /** @return Hash of the wrapped formatter, consistent with [equals]. */
    override fun hashCode(): Int = format.hashCode()
}

/**
 * Offset mapping for an insertion-only formatter. Walks the transformed string and consumes
 * original chars as they appear; positions "stick" to the next original char so the caret
 * lands past inserted separators after typing, which is the natural behavior.
 *
 * @property original Raw text held in state.
 * @property transformed Same text after the formatter inserted its decorations.
 */
private class FormatOffsetMapping(
    private val original: String,
    private val transformed: String,
) : OffsetMapping {

    /**
     * @param offset Position in [original]; coerced into range rather than rejected.
     * @return Matching position in [transformed], placed after any separator inserted at that
     *   point so the caret does not sit before a character the user cannot delete.
     */
    override fun originalToTransformed(offset: Int): Int {
        val target = offset.coerceIn(0, original.length)
        var consumed = 0
        var t = 0
        // Advance t until we've consumed `target` original chars.
        while (consumed < target && t < transformed.length) {
            if (consumed < original.length && transformed[t] == original[consumed]) consumed++
            t++
        }
        // Skip any inserted chars that sit between the target and the next original char,
        // so the caret lands after the inserted separator rather than before it.
        if (target < original.length) {
            val nextOriginal = original[target]
            while (t < transformed.length && transformed[t] != nextOriginal) t++
        }
        return t
    }

    /**
     * @param offset Position in [transformed]; coerced into range rather than rejected.
     * @return Matching position in [original], counting only characters that survived from it.
     */
    override fun transformedToOriginal(offset: Int): Int {
        val target = offset.coerceIn(0, transformed.length)
        var consumed = 0
        var t = 0
        while (t < target) {
            if (consumed < original.length && transformed[t] == original[consumed]) consumed++
            t++
        }
        return consumed
    }
}

/**
 * Chains two [VisualTransformation]s: the receiver runs first, [other] runs on its output.
 * Offsets compose in the same order: original → first → second.
 *
 * @param other Transformation applied to the receiver's output.
 * @return The composed transformation; the non-identity side when either is
 *   [VisualTransformation.None].
 */
internal fun VisualTransformation.then(other: VisualTransformation): VisualTransformation {
    if (other === VisualTransformation.None) return this
    if (this === VisualTransformation.None) return other
    val outer = this
    return VisualTransformation { text ->
        val a = outer.filter(text)
        val b = other.filter(a.text)
        TransformedText(
            b.text,
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    b.offsetMapping.originalToTransformed(
                        a.offsetMapping.originalToTransformed(
                            offset
                        )
                    )

                override fun transformedToOriginal(offset: Int): Int =
                    a.offsetMapping.transformedToOriginal(
                        b.offsetMapping.transformedToOriginal(
                            offset
                        )
                    )
            },
        )
    }
}
