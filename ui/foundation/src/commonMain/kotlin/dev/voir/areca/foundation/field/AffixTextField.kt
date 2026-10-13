package dev.voir.areca.foundation.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Single-line text field with optional prefix/suffix that sit next to the typed value.
 * Affixes are decoration — they cannot be edited or deleted.
 *
 * Visibility rule: affixes appear when the field is focused OR has a value. Override with
 * [alwaysShowAffixes]. `format` is display-only — state holds the raw value.
 *
 * Always single-line: the layout pins the editor to the measured width of one line of text so
 * that the suffix can sit against it, which a wrapping value has no single width for.
 *
 * @param value Raw text in your state.
 * @param onValueChange Called with the raw text the user produced.
 * @param modifier Modifier applied to the field container.
 * @param prefix Optional composable rendered before the value.
 * @param suffix Optional composable rendered after the value.
 * @param affixGap Visual gap between the typed value and each adjacent affix.
 * @param format Optional beautifier for display (e.g. phone mask). Does not alter state.
 * @param alwaysShowAffixes When true, affixes are visible even if empty and unfocused.
 * @param style Color palette.
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
 * @param placeholder Shown between prefix and the caret when the value is empty.
 * @param keyboardOptions Software keyboard configuration.
 * @param keyboardActions IME action handlers.
 * @param visualTransformation Extra display transform. If [format] is set, it runs first.
 * @param cursorBrush Brush painting the caret.
 * @param interactionSource Hoisted interaction source. Pass one to observe focus and presses;
 *   `null` keeps one internally.
 */
@Composable
public fun AffixTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    affixGap: Dp = ArecaTheme.spacing.xs2,
    format: ((String) -> String)? = null,
    alwaysShowAffixes: Boolean = false,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val hovered by interactions.collectIsHoveredAsState()
    val showAffixes = alwaysShowAffixes || focused || value.isNotEmpty()

    val visualState = fieldVisualState(status, enabled, readOnly, focused, hovered)
    val contentColor by animateColorAsState(
        targetValue = resolveContentColor(style = style, state = visualState),
        animationSpec = fieldColorSpec(),
        label = "affixFieldContent",
    )
    // The placeholder is drawn here rather than by [Field], so it resolves its own color from the
    // same state the container does: dimmed when disabled, tinted under an error.
    val placeholderColor by animateColorAsState(
        targetValue = resolvePlaceholderColor(style = style, state = visualState),
        animationSpec = fieldColorSpec(),
        label = "affixFieldPlaceholder",
    )

    // Internal TFV to preserve selection. No format rewriting — format is visual only.
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

    // Measure the *displayed* text ourselves so we can pin the inner slot to its exact width.
    // Without this, BasicTextField's inner scroll container claims the full available width
    // (`defaultMinSize(280.dp)` + scrollable viewport fills max), pushing the suffix far right.
    val textMeasurer = rememberTextMeasurer()
    val textStyle = size.textStyle
    val displayedText = remember(value, format, visualTransformation) {
        val afterFormat = format?.invoke(value) ?: value
        if (visualTransformation === VisualTransformation.None) afterFormat
        else visualTransformation.filter(AnnotatedString(afterFormat)).text.text
    }
    // Keyed on the measurer too: it is rebuilt when the density, the layout direction or the font
    // resolver changes, and a width cached across that change would be measured in the old units.
    val textWidthPx = remember(displayedText, textStyle, textMeasurer) {
        if (displayedText.isEmpty()) 0
        else textMeasurer.measure(
            text = AnnotatedString(displayedText),
            style = textStyle,
            overflow = TextOverflow.Clip,
            softWrap = false,
            maxLines = 1,
        ).size.width
    }

    BasicTextField(
        value = current,
        onValueChange = { new ->
            tfvState = new
            if (new.text != value) onValueChange(new.text)
        },
        modifier = modifier.fieldControl(field),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.merge(color = contentColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        visualTransformation = effectiveVT,
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
                empty = value.isEmpty(),
                leading = leading,
                trailing = trailing,
                // Placeholder is rendered inline so it sits next to the caret, not at the edge.
                placeholder = null,
                interactionSource = interactions,
                content = {
                    AffixRow(
                        prefix = if (showAffixes) prefix else null,
                        suffix = if (showAffixes) suffix else null,
                        placeholder = if (value.isEmpty()) placeholder else null,
                        gap = affixGap,
                        affixColor = style.extra,
                        placeholderColor = placeholderColor,
                        size = size,
                        textWidthPx = textWidthPx,
                        innerTextField = innerTextField,
                    )
                },
            )
        },
    )
}

/**
 * Lays out prefix, placeholder (when empty), the inner text slot, and suffix in a single row.
 *
 * Why we can't just use a Row + `minWidth = 0`:
 *  `BasicTextField`'s inner text slot wraps the text in a horizontally-scrollable container
 *  that claims the full available maxWidth regardless of how narrow the text is. Giving it
 *  loose constraints doesn't help — it fills whatever we give it.
 *
 * Fix: we pre-measure the displayed text with [rememberTextMeasurer] up in [AffixTextField] and
 * pin the inner slot to that exact width (plus a tiny buffer for the caret). The suffix is then
 * placed immediately after, with only [gap] in between.
 *
 * When the field is empty, we fall back to the placeholder's width so the suffix sits past the
 * placeholder rather than crashing into it.
 *
 * @param prefix Decoration placed before the value, already filtered for visibility.
 * @param suffix Decoration placed after the value, already filtered for visibility.
 * @param placeholder Hint drawn at the caret position, or null when the field has a value.
 * @param gap Space between the value and each adjacent affix.
 * @param affixColor Color of the prefix and suffix.
 * @param placeholderColor Color of the placeholder, resolved for the field's current state.
 * @param size Metrics the affixes and placeholder take their type from.
 * @param textWidthPx Width of the displayed text in pixels, measured by the caller. The inner
 *   slot is pinned to it because it would otherwise claim every pixel it is offered.
 * @param innerTextField The editor slot handed down by [BasicTextField].
 */
@Composable
private fun AffixRow(
    prefix: (@Composable () -> Unit)?,
    suffix: (@Composable () -> Unit)?,
    placeholder: (@Composable () -> Unit)?,
    gap: Dp,
    affixColor: Color,
    placeholderColor: Color,
    size: FieldSize,
    textWidthPx: Int,
    innerTextField: @Composable () -> Unit,
) {
    SubcomposeLayout { constraints ->
        val gapPx = gap.roundToPx()
        val cursorBufferPx = CaretWidth.roundToPx()
        val looseC = constraints.copy(minWidth = 0, minHeight = 0)

        val prefixPlaceable = prefix?.let {
            subcompose("prefix") {
                CompositionLocalProvider(
                    LocalContentColor provides affixColor,
                    LocalTextStyle provides size.textStyle,
                ) { prefix() }
            }.first().measure(looseC)
        }
        val suffixPlaceable = suffix?.let {
            subcompose("suffix") {
                CompositionLocalProvider(
                    LocalContentColor provides affixColor,
                    LocalTextStyle provides size.textStyle,
                ) { suffix() }
            }.first().measure(looseC)
        }

        val prefixW = prefixPlaceable?.width ?: 0
        val suffixW = suffixPlaceable?.width ?: 0
        val leftGap = if (prefixPlaceable != null) gapPx else 0
        val rightGap = if (suffixPlaceable != null) gapPx else 0

        val availableForInner =
            (constraints.maxWidth - prefixW - suffixW - leftGap - rightGap).coerceAtLeast(0)

        // Pre-measured text width + caret buffer, clamped to the space we actually have.
        val innerWidthPx = (textWidthPx + cursorBufferPx).coerceIn(0, availableForInner)

        // Fixed-width constraint: forces the scrollable inner to be exactly as wide as the text.
        val innerPlaceable = subcompose("inner") { innerTextField() }
            .first()
            .measure(
                Constraints(
                    minWidth = innerWidthPx,
                    maxWidth = innerWidthPx,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight,
                )
            )

        val placeholderPlaceable = placeholder?.let {
            subcompose("placeholder") {
                CompositionLocalProvider(
                    LocalContentColor provides placeholderColor,
                    LocalTextStyle provides size.textStyle,
                ) { it() }
            }.firstOrNull()?.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = availableForInner,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight,
                )
            )
        }

        // The slot that carries the caret / placeholder is as wide as whichever is larger,
        // so the suffix doesn't collide with the placeholder on an empty field.
        val centerSlotW = maxOf(innerPlaceable.width, placeholderPlaceable?.width ?: 0)

        val rowHeight = maxOf(
            prefixPlaceable?.height ?: 0,
            innerPlaceable.height,
            placeholderPlaceable?.height ?: 0,
            suffixPlaceable?.height ?: 0,
            constraints.minHeight,
        )
        val rowWidth = (prefixW + leftGap + centerSlotW + rightGap + suffixW)
            .coerceAtMost(constraints.maxWidth)

        layout(rowWidth, rowHeight) {
            var x = 0
            prefixPlaceable?.placeRelative(x, (rowHeight - prefixPlaceable.height) / 2)
            x += prefixW + leftGap

            // Placeholder and inner share the same origin; placeholder shows through when empty.
            placeholderPlaceable?.placeRelative(x, (rowHeight - placeholderPlaceable.height) / 2)
            innerPlaceable.placeRelative(x, (rowHeight - innerPlaceable.height) / 2)
            x += centerSlotW + rightGap

            suffixPlaceable?.placeRelative(x, (rowHeight - suffixPlaceable.height) / 2)
        }
    }
}

/**
 * Room left after the measured text for the caret, which [BasicTextField] draws 2dp wide at the
 * end of the value. Without it the slot pinned to the text's width would clip the caret.
 */
private val CaretWidth = 2.dp
