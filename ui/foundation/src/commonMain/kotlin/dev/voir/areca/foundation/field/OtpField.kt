package dev.voir.areca.foundation.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * A one-time code, typed one character to a cell.
 *
 * Built for the screen that says "enter the 6-digit code we sent": a row of cells, the one that
 * takes the next character ringed and carrying a caret, the rest showing what has been typed.
 * Underneath is a single ordinary text editor, so everything the platform does for a text box
 * still works — pasting a whole code, the keyboard's own suggestion of a code from a message,
 * autofill, deleting with backspace — and a screen reader hears one field holding one value
 * rather than six fields holding a digit each.
 *
 * **The cells are field containers.** Each is drawn from the same [FieldStyle] a [TextField] is,
 * so `outlined`, `soft`, `filled` and `underlined` all apply, and a status reaches every cell at
 * once: a wrong code turns the whole row red, not the cell that happened to be last.
 *
 * **Input is filtered, not validated.** [filter] decides which characters can be typed at all —
 * digits by default — and the field stops at [length]. A pasted `"123-456"` arrives as
 * `"123456"`. Whether the code is *right* is not the field's business: report that through
 * [status], or through the error of an enclosing [FieldGroup].
 *
 * The caret always sits after the last character. A code is typed and erased from its end, and
 * a caret that could be dropped into the middle of one only offers a way to get it wrong.
 *
 * Wrap it in a [FieldGroup] for a label and a message, and forward the scope:
 *
 * ```kotlin
 * FieldGroup(label = "Verification code", error = codeError) { field ->
 *     OtpField(
 *         value = code,
 *         onValueChange = { code = it },
 *         onComplete = ::verify,
 *         field = field,
 *         stretch = true,
 *     )
 * }
 * ```
 *
 * A label placed inside the container has no single container to go in, so an internal
 * [FieldLabelPlacement] draws no label here; use one of the external placements.
 *
 * The field sets no autofill content type, because it cannot tell a one-time code from a PIN.
 * Set `ContentType.SmsOtpCode` through [modifier] when it holds a code that was sent somewhere.
 *
 * @param value The code as typed so far. Characters [filter] rejects and anything past [length]
 *   are not drawn.
 * @param onValueChange Called with the new code on every edit, already filtered and cut to
 *   [length].
 * @param modifier Modifier applied to the editor, which spans the row of cells.
 * @param length Number of cells, and the most characters the code can hold.
 * @param onComplete Called with the code when an edit fills the last cell — typed, pasted or
 *   autofilled. The place to start verifying, so nobody has to press a button to say they are
 *   done. Not called when the code is filled by the caller rather than by an edit.
 * @param style Colors and shape of every cell. See [FieldStyle].
 * @param size Cell metrics and type. See [OtpFieldSize].
 * @param field Scope from an enclosing [FieldGroup]: the status to report, what to announce the
 *   field as, and the handle a tap on the group's label moves focus through.
 * @param status Validation outcome, drawn on every cell. Comes from [field] unless it is given
 *   explicitly.
 * @param enabled When false, non-interactive and drawn as disabled — while a code is being
 *   verified, say. Comes from [field] unless it is given explicitly.
 * @param readOnly Visible but not editable.
 * @param masked Whether typed characters are drawn as dots — for a PIN rather than a code that
 *   arrived in a message.
 * @param stretch Whether the cells divide the width the field is given between them, rather than
 *   each taking [OtpFieldSize.cellWidth]. Pass a width through [modifier] when stretching.
 * @param groupSize Cells per group, or `null` for one run. `3` draws a six-character code as two
 *   groups of three, the way it is usually printed in the message.
 * @param separator Drawn between groups, or `null` for a wider gap alone.
 * @param placeholder Drawn in each empty cell that is not taking the next character, or `null`
 *   for empty cells.
 * @param filter Which characters can be typed. Pass `Char::isLetterOrDigit` for an alphanumeric
 *   code, together with a [keyboardOptions] that offers letters.
 * @param keyboardOptions Software keyboard configuration. Defaults to a number pad whose action
 *   is Done.
 * @param keyboardActions IME action handlers.
 * @param cursorBrush Brush painting the caret in the cell that takes the next character.
 * @param interactionSource Source of the field's interactions; pass one to observe focus from
 *   outside, or `null` to let the field create its own.
 */
@Composable
public fun OtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    onComplete: ((String) -> Unit)? = null,
    style: FieldStyle = FieldDefaults.style,
    size: OtpFieldSize = LocalOtpFieldSize.current ?: OtpFieldSize.Default,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    masked: Boolean = false,
    stretch: Boolean = false,
    groupSize: Int? = null,
    separator: (@Composable () -> Unit)? = null,
    placeholder: Char? = null,
    filter: (Char) -> Boolean = Char::isDigit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done,
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null,
) {
    require(length > 0) { "An OtpField needs at least one cell, not $length." }
    require(groupSize == null || groupSize > 0) { "A group holds at least one cell, not $groupSize." }

    val code = otpCode(value, length, filter)
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val currentOnComplete by rememberUpdatedState(onComplete)

    // Rebuilt from the code every time, which is what pins the caret to the end: a tap that moves
    // it, or a drag that selects, changes nothing the field reports and is undone on the next frame.
    val editorValue = TextFieldValue(text = code, selection = TextRange(code.length))

    // The editor draws nothing of its own, and a selection handle it has no visible text to point
    // at is worse than none.
    CompositionLocalProvider(LocalTextSelectionColors provides HiddenSelectionColors) {
        BasicTextField(
            value = editorValue,
            onValueChange = { next ->
                val typed = otpCode(next.text, length, filter)
                if (typed != code) {
                    onValueChange(typed)
                    if (typed.length == length) currentOnComplete?.invoke(typed)
                }
            },
            modifier = modifier.fieldControl(field),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = size.textStyle.merge(color = Color.Transparent),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            // Masking changes what assistive technology is told, not what is drawn: the cells draw
            // their own dots.
            visualTransformation = if (masked) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(Color.Transparent),
            interactionSource = interactions,
            decorationBox = { editor ->
                Box(contentAlignment = Alignment.Center) {
                    OtpCells(
                        code = code,
                        length = length,
                        activeIndex = if (focused && enabled && !readOnly) otpActiveIndex(code.length, length) else null,
                        style = style,
                        size = size,
                        status = status,
                        enabled = enabled,
                        readOnly = readOnly,
                        masked = masked,
                        stretch = stretch,
                        groupSize = groupSize,
                        separator = separator,
                        placeholder = placeholder,
                        cursorBrush = cursorBrush,
                    )

                    // Still composed, because it is what the keyboard types into and what the
                    // platform focuses; it is only never seen.
                    Box(modifier = Modifier.matchParentSize().alpha(0f)) { editor() }
                }
            },
        )
    }
}

/**
 * The row of cells an [OtpField] draws in place of its editor.
 *
 * @param code Filtered code to draw.
 * @param length Number of cells.
 * @param activeIndex Cell taking the next character, or `null` while the field is not taking
 *   input.
 * @param style Cell colors and shape.
 * @param size Cell metrics.
 * @param status Validation outcome, drawn on every cell.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the value can be changed.
 * @param masked Whether characters are drawn as dots.
 * @param stretch Whether the cells share the row's width.
 * @param groupSize Cells per group, or `null` for one run.
 * @param separator Drawn between groups, or `null` for a wider gap.
 * @param placeholder Drawn in empty cells, or `null` for none.
 * @param cursorBrush Brush painting the caret.
 */
@Composable
private fun OtpCells(
    code: String,
    length: Int,
    activeIndex: Int?,
    style: FieldStyle,
    size: OtpFieldSize,
    status: FieldStatus,
    enabled: Boolean,
    readOnly: Boolean,
    masked: Boolean,
    stretch: Boolean,
    groupSize: Int?,
    separator: (@Composable () -> Unit)?,
    placeholder: Char?,
    cursorBrush: Brush,
) {
    // The editor underneath already says what the field holds; the cells saying it again would
    // have a screen reader read the code twice.
    Row(
        modifier = (if (stretch) Modifier.fillMaxWidth() else Modifier).clearAndSetSemantics {},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (index in 0 until length) {
            if (index > 0) {
                val startsGroup = groupSize != null && index % groupSize == 0
                when {
                    !startsGroup -> Spacer(Modifier.width(size.gap))
                    separator != null -> {
                        Spacer(Modifier.width(size.gap))
                        separator()
                        Spacer(Modifier.width(size.gap))
                    }
                    else -> Spacer(Modifier.width(size.groupGap))
                }
            }

            OtpCell(
                character = code.getOrNull(index),
                active = index == activeIndex,
                style = style,
                size = size,
                status = status,
                enabled = enabled,
                readOnly = readOnly,
                masked = masked,
                placeholder = placeholder,
                cursorBrush = cursorBrush,
                modifier = if (stretch) Modifier.weight(1f) else Modifier.width(size.cellWidth),
            )
        }
    }
}

/**
 * One cell of an [OtpField].
 *
 * Resolved exactly as a [Field] is — one [FieldVisualState], every color mapped from it and
 * animated between — with the cell taking the next character standing in for a field that holds
 * focus.
 *
 * @param character Character in this cell, or `null` when it is empty.
 * @param active Whether this cell takes the next character.
 * @param style Colors and shape.
 * @param size Metrics.
 * @param status Validation outcome.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the value can be changed.
 * @param masked Whether [character] is drawn as a dot.
 * @param placeholder Drawn while the cell is empty and not [active], or `null` for none.
 * @param cursorBrush Brush painting the caret while the cell is [active] and empty.
 * @param modifier Modifier carrying the cell's width.
 */
@Composable
private fun OtpCell(
    character: Char?,
    active: Boolean,
    style: FieldStyle,
    size: OtpFieldSize,
    status: FieldStatus,
    enabled: Boolean,
    readOnly: Boolean,
    masked: Boolean,
    placeholder: Char?,
    cursorBrush: Brush,
    modifier: Modifier = Modifier,
) {
    val state = fieldVisualState(status = status, enabled = enabled, readOnly = readOnly, focused = active)

    val colorSpec = fieldColorSpec()
    val background by animateColorAsState(
        targetValue = resolveBackgroundColor(style, state),
        animationSpec = colorSpec,
        label = "otpCellBg",
    )
    val border by animateColorAsState(
        targetValue = resolveBorderColor(style, state),
        animationSpec = colorSpec,
        label = "otpCellBorder",
    )
    val content by animateColorAsState(
        targetValue = resolveContentColor(style, state),
        animationSpec = colorSpec,
        label = "otpCellContent",
    )
    val ring by animateColorAsState(
        targetValue = resolveFocusRingColor(style, state, active),
        animationSpec = colorSpec,
        label = "otpCellRing",
    )

    Box(
        modifier = modifier
            .height(size.cellHeight)
            .fieldContainer(
                style = style,
                borderWidth = size.borderWidth,
                focusRingWidth = size.focusRing,
                bg = background,
                border = border,
                focusRing = ring,
            ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            character != null -> Text(
                text = if (masked) MaskGlyph.toString() else character.toString(),
                style = size.textStyle,
                color = content,
            )

            active -> OtpCaret(brush = cursorBrush, size = size)

            placeholder != null -> Text(
                text = placeholder.toString(),
                style = size.textStyle,
                color = resolvePlaceholderColor(style, state),
            )
        }
    }
}

/**
 * The blinking bar in the cell that takes the next character.
 *
 * Blinks on the same half-second rhythm as a platform caret, so the field reads as a text box
 * waiting for input rather than as a decoration.
 *
 * @param brush Brush painting the bar.
 * @param size Metrics carrying the bar's width and height.
 */
@Composable
private fun OtpCaret(brush: Brush, size: OtpFieldSize) {
    val transition = rememberInfiniteTransition(label = "otpCaret")
    val visibility by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = CaretPeriodMillis
                1f at 0
                1f at CaretPeriodMillis / 2 - 1
                0f at CaretPeriodMillis / 2
                0f at CaretPeriodMillis - 1
            },
        ),
        label = "otpCaretVisibility",
    )

    Box(
        modifier = Modifier
            .size(width = size.caretWidth, height = size.caretHeight)
            .graphicsLayer { alpha = visibility }
            .background(brush),
    )
}

/**
 * Filters typed text into a code: only accepted characters, and no more than fit.
 *
 * @param text Text as it arrived — typed, pasted or autofilled.
 * @param length Most characters the code holds.
 * @param filter Which characters are accepted.
 * @return The accepted characters, in order, cut to [length].
 */
internal fun otpCode(text: String, length: Int, filter: (Char) -> Boolean): String = buildString {
    for (character in text) {
        if (this.length == length) break
        if (filter(character)) append(character)
    }
}

/**
 * Which cell takes the next character.
 *
 * The cell after the last one filled, or the last cell once every cell is filled — a full code
 * still shows where a backspace would land.
 *
 * @param filled Characters typed so far.
 * @param length Number of cells.
 * @return Index of the cell, from zero.
 */
internal fun otpActiveIndex(filled: Int, length: Int): Int = filled.coerceIn(0, length - 1)

/** Drawn in place of each character of a masked code. */
private const val MaskGlyph = '•'

/** One full on-and-off cycle of the caret. */
private const val CaretPeriodMillis = 1000

/** Selection colors that draw nothing, for an editor that is never seen. */
private val HiddenSelectionColors = TextSelectionColors(
    handleColor = Color.Transparent,
    backgroundColor = Color.Transparent,
)
