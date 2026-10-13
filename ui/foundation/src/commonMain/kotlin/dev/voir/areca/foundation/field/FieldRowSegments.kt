package dev.voir.areca.foundation.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.theme.ArecaTheme

/**
 * A [FieldRowScope.Segment] the user types into.
 *
 * The editor rather than the whole field: the container, the border and the state colors belong to
 * the [FieldRow] around it, so an amount typed on the left and a currency chosen on the right are
 * one field that happens to have two halves.
 *
 * Weighted by default, and that default is load-bearing — the editor asks for a generous minimum
 * width of its own, so an unweighted text segment pushes everything else out of the row.
 *
 * `format` is display-only, exactly as in [TextField]: your state keeps the raw value and the
 * segment shows the formatted one, with the caret remapped so it does not jump. Filter input in
 * [onValueChange].
 *
 * @param value Raw text in your state.
 * @param onValueChange Called with the raw text the user produced, before formatting.
 * @param modifier Modifier applied to the segment.
 * @param weight Share of the row's leftover width.
 * @param enabled Whether this segment accepts input. Ignored while the row is disabled.
 * @param readOnly Visible and selectable but not editable. Ignored while the row is read-only.
 * @param format Display-only beautifier applied to the raw value.
 * @param padding Inset between the segment's edges and the editor. Defaults to the row's
 *   [FieldRowScope.contentPadding].
 * @param loading Replaces the trailing slot with a spinner.
 * @param leading Slot pinned to the segment's start edge.
 * @param trailing Slot pinned to the segment's end edge. Hidden while [loading].
 * @param placeholder Hint drawn while the segment is empty.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardActions IME action handlers.
 * @param visualTransformation Extra display transform. [format] runs first when both are given.
 * @param singleLine One-line editor. A composite row divides horizontally, so a segment that
 *   wrapped would push the dividers past everything beside it — leave this on unless the whole
 *   row is meant to grow.
 * @param cursorBrush Brush painting the caret.
 * @param interactionSource Hoisted interaction source. The row watches focus through the focus
 *   system rather than through this, so a caller is free to use it for its own purposes.
 */
@Composable
public fun FieldRowScope.TextSegment(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    weight: Float = 1f,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    format: ((String) -> String)? = null,
    padding: PaddingValues = contentPadding,
    loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val editable = this.enabled && enabled
    val locked = this.readOnly || readOnly
    val textStyle = size.textStyle.merge(color = LocalContentColor.current)

    // Mirrors [TextField]'s String overload: selection is kept here so it survives recomposition,
    // while the hoisted value stays the owner and collapses the caret whenever the two disagree.
    var tfvState by remember { mutableStateOf(TextFieldValue(value, TextRange(value.length))) }
    val current = if (tfvState.text == value) {
        tfvState
    } else {
        tfvState.copy(text = value, selection = TextRange(value.length), composition = null)
    }

    val effectiveTransformation = remember(format, visualTransformation) {
        when {
            format == null -> visualTransformation
            visualTransformation === VisualTransformation.None ->
                FormatVisualTransformation(format)

            else -> FormatVisualTransformation(format).then(visualTransformation)
        }
    }

    Segment(
        modifier = modifier,
        weight = weight,
        enabled = enabled,
        padding = padding,
        loading = loading,
        empty = value.isEmpty(),
        leading = leading,
        trailing = trailing,
        placeholder = placeholder,
    ) {
        BasicTextField(
            value = current,
            onValueChange = { new ->
                tfvState = new
                if (new.text != value) onValueChange(new.text)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = editable,
            readOnly = locked,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            visualTransformation = effectiveTransformation,
            cursorBrush = cursorBrush,
            interactionSource = interactionSource,
        )
    }
}
