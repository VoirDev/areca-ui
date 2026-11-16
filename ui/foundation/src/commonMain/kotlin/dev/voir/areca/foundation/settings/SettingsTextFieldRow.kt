package dev.voir.areca.foundation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.LocalListEnabled
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.firstLineCenter
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/**
 * A settings row that is typed into in place: a name, a bio, a device label.
 *
 * The field has no box of its own. The section card is the field, and the separator under the row
 * is its underline, so a group of fields reads as one form — "First name" over "Last name" — rather
 * than as a stack of inputs dropped onto a card. Name the group with the [Section] header and let
 * [placeholder] say what each row holds.
 *
 * With [maxLength], input past the limit is cut off rather than refused mid-word, and by default the
 * row counts down the characters that are left in the bottom corner. Characters are counted as
 * Unicode code points, so an emoji or another character outside the Basic Multilingual Plane counts
 * once and is never cut in half.
 *
 * While [status] is [SettingsRowStatus.Loading] the field stops accepting edits and a spinner takes
 * the counter's place; an error message appears under the field when the change failed.
 *
 * **Labelled form rows.** Give the row a [title] and it becomes "label on the left, input on the
 * right" — "Model ……… ThinkPad", the shape of an item form. The title keeps to one line at the
 * start, the typed text runs against the end edge, and tapping the title focuses the field. A
 * [suffix] after the input names its unit, "Width ……… 80 cm"; make it a [SettingsTextFieldSuffix]
 * with an `onClick` to let the unit be changed from there. Without a title the row is the boxless
 * field described above, and a suffix sits after it all the same.
 *
 * @param value Current text.
 * @param onValueChange Called with the edited text, already cut to [maxLength].
 * @param placeholder What the row holds, shown while [value] is empty and read by assistive
 * technology when there is no [title]. Pass `""` for a titled row that needs no hint.
 * @param modifier Modifier applied to the row.
 * @param leading Slot before the field.
 * @param title Label at the start of the row, already localized, or `null` for a row that is the
 * field alone. With a title the input is aligned to the end and the title is what assistive
 * technology reads for the field. A string rather than a slot, unlike other settings rows' titles,
 * because it is the field's accessible name.
 * @param suffix Slot right after the input, usually a [SettingsTextFieldSuffix] naming the unit,
 * [ListItemSize.valueGap] after it. It is given the row's value color and title text style.
 * @param maxLength Most characters the field accepts, or `null` for no limit. Must not be negative.
 * @param showCounter Whether to count down the characters left before [maxLength]; ignored without
 * a limit.
 * @param singleLine Whether the field stays on one line; `false` lets it grow, as for a bio.
 * @param maxLines Most lines a multi-line field grows to before it scrolls.
 * @param status Progress of the change the row last asked for.
 * @param enabled Whether the field accepts input. A field in a disabled [Section] is disabled
 * regardless.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset], for a setting that
 * depends on the one above it.
 * @param keyboardOptions Software keyboard options, such as capitalization or the IME action.
 * @param keyboardActions Actions run by the IME action key.
 * @param interactionSource Source of the field's interactions; pass one to observe focus from
 * outside.
 * @param style Colors of the row; the text takes its title colors and the leading slot its extra
 * colors.
 * @param size Metrics and typography of the row.
 * @throws IllegalArgumentException When [maxLength] is negative.
 */
@Composable
public fun SettingsTextFieldRow(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    title: String? = null,
    suffix: (@Composable () -> Unit)? = null,
    maxLength: Int? = null,
    showCounter: Boolean = maxLength != null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    enabled: Boolean = true,
    nestingLevel: Int = 0,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row,
    size: ListItemSize = SettingsTheme.sizes.row,
) {
    require(maxLength == null || maxLength >= 0) { "maxLength must not be negative, was $maxLength" }

    val styles = SettingsTheme.styles
    val statusStyle = SettingsTheme.sizes.statusTextStyle
    val rowEnabled = enabled && LocalListEnabled.current
    val loading = status == SettingsRowStatus.Loading
    val anchor = rememberSeparatorAnchor()
    val layoutDirection = LocalLayoutDirection.current

    val textColor = if (rowEnabled) style.title else style.titleDisabled
    val extraColor = if (rowEnabled) style.extra else style.extraDisabled
    val valueColor = if (rowEnabled) style.value else style.valueDisabled
    val focusRequester = remember { FocusRequester() }
    val textAlign = if (title != null) TextAlign.End else TextAlign.Start

    val counter: (@Composable () -> Unit)? = if (maxLength != null && showCounter) {
        {
            val length = value.codePointLength()
            Text(
                text = (maxLength - length).coerceAtLeast(0).toString(),
                style = statusStyle,
                color = when {
                    !rowEnabled -> style.titleDisabled
                    length >= maxLength -> ArecaTheme.colors.fgWarnText
                    else -> styles.placeholder
                },
            )
        }
    } else {
        null
    }
    val end = statusValue(status, counter)
    val bottom = statusBottom(status, bottom = null)

    Column(
        modifier = Modifier
            .separatorAnchor(anchor)
            .then(modifier)
            .fillMaxWidth()
            .background(if (rowEnabled) style.bg else style.bgDisabled, style.shape)
            .padding(
                start = size.contentPadding.calculateStartPadding(layoutDirection) +
                    size.nestedLevelOffset * nestingLevel,
                top = size.contentPadding.calculateTopPadding(),
                end = size.contentPadding.calculateEndPadding(layoutDirection),
                bottom = size.contentPadding.calculateBottomPadding(),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = size.minHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Aligned like every other row: centred while the field is a line or two, and from the
            // top once a bio grows past that, so the glyph stays with the start of the text.
            SlotRow(
                modifier = Modifier.weight(1f),
                layout = SlotRowLayout.Auto.copy(startGap = size.leadingGap),
                start = leading?.let { slot ->
                    {
                        CompositionLocalProvider(LocalContentColor provides extraColor) {
                            Box(contentAlignment = Alignment.Center, content = slot)
                        }
                    }
                },
                center = {
                    val field: @Composable (Modifier) -> Unit = { fieldModifier ->
                        BasicTextField(
                            value = value,
                            onValueChange = { text ->
                                onValueChange(if (maxLength != null) text.takeCodePoints(maxLength) else text)
                            },
                            modifier = fieldModifier
                                .focusRequester(focusRequester)
                                .firstLineCenter(size.titleTextStyle)
                                .semantics { contentDescription = title ?: placeholder },
                            enabled = rowEnabled,
                            readOnly = loading,
                            textStyle = size.titleTextStyle.merge(color = textColor, textAlign = textAlign),
                            keyboardOptions = keyboardOptions,
                            keyboardActions = keyboardActions,
                            singleLine = singleLine,
                            maxLines = maxLines,
                            interactionSource = interactionSource,
                            cursorBrush = SolidColor(styles.cursor),
                            decorationBox = { field ->
                                if (value.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        modifier = Modifier.fillMaxWidth(),
                                        style = size.titleTextStyle,
                                        textAlign = textAlign,
                                        color = if (rowEnabled) styles.placeholder else style.titleDisabled,
                                        maxLines = maxLines,
                                    )
                                }
                                field()
                            },
                        )
                    }

                    if (title == null && suffix == null) {
                        field(Modifier.fillMaxWidth().separatorText(anchor))
                    } else Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                modifier = Modifier
                                    // Measured before the field, at most half the row, so a long
                                    // label ellipsises and the field keeps the rest of the width.
                                    .layout { measurable, constraints ->
                                        val cap = if (constraints.hasBoundedWidth) {
                                            (constraints.maxWidth * TITLE_MAX_FRACTION).toInt()
                                        } else {
                                            constraints.maxWidth
                                        }
                                        val placeable = measurable.measure(
                                            constraints.copy(minWidth = 0, maxWidth = cap),
                                        )
                                        layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                                    }
                                    .separatorText(anchor)
                                    .firstLineCenter(size.titleTextStyle)
                                    // The label is the field's name, already read through the
                                    // field; a tap on it focuses the field, as on any form.
                                    .clearAndSetSemantics {}
                                    .pointerInput(rowEnabled) {
                                        if (rowEnabled) detectTapGestures { focusRequester.requestFocus() }
                                    },
                                style = size.titleTextStyle,
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.width(size.trailingGap))
                            field(Modifier.weight(1f))
                        } else {
                            field(Modifier.weight(1f).separatorText(anchor))
                        }

                        if (suffix != null) {
                            Spacer(modifier = Modifier.width(size.valueGap))
                            CompositionLocalProvider(
                                LocalContentColor provides valueColor,
                                LocalTextStyle provides size.titleTextStyle,
                                LocalListEnabled provides rowEnabled,
                                content = suffix,
                            )
                        }
                    }
                },
            )

            if (end != null) {
                Spacer(modifier = Modifier.width(size.trailingGap))
                Box(
                    // The counter sits in the bottom corner of a field that grows; the spinner,
                    // which stands for the whole value, stays centred on the line.
                    modifier = Modifier.align(
                        if (loading) Alignment.CenterVertically else Alignment.Bottom,
                    ),
                ) {
                    end()
                }
            }
        }

        if (bottom != null) {
            Spacer(modifier = Modifier.height(size.bottomGap))
            bottom()
        }
    }
}

/**
 * The unit after the input of a [SettingsTextFieldRow] — "cm", "kg", "pcs" — and, with [onClick], the
 * way to change it.
 *
 * Plain, it is quiet text in the row's value color. Pressable, it takes the settings list's
 * interactive value color and a small chevron, and announces itself as a dropdown, so it reads as
 * the control it is; anchor a dropdown to it through [modifier], or open a sheet from [onClick].
 * It follows the row's enabled state.
 *
 * @param text The unit, already localized.
 * @param modifier Modifier applied to the suffix — where to anchor a menu that changes the unit.
 * @param onClick Called when the unit is pressed, or `null` for a unit that cannot be changed here.
 * @param onClickLabel What pressing does, for assistive technology — "Change unit".
 */
@Composable
public fun SettingsTextFieldSuffix(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
) {
    val enabled = LocalListEnabled.current
    val interactive = SettingsTheme.styles.interactiveValue
    val color = if (onClick != null && enabled && interactive.isSpecified) interactive else LocalContentColor.current

    Row(
        modifier = modifier.thenIf(onClick != null) {
            clickCursor(enabled)
                .clickable(
                    enabled = enabled,
                    onClickLabel = onClickLabel,
                    role = Role.DropdownList,
                    interactionSource = null,
                    indication = FadeIndication(),
                    onClick = { onClick?.invoke() },
                )
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
    ) {
        Text(text = text, color = color, maxLines = 1)
        if (onClick != null) {
            Chevron(direction = ChevronDirection.Down, color = color, size = ChevronSize.Small)
        }
    }
}

/** Most of a labelled row's width its title may take before it ellipsises. */
private const val TITLE_MAX_FRACTION = 0.5f

/**
 * Number of Unicode code points in this string: a surrogate pair counts once.
 *
 * @return Code point count.
 */
internal fun String.codePointLength(): Int {
    var count = 0
    var index = 0
    while (index < length) {
        index += if (isSurrogatePairAt(index)) 2 else 1
        count++
    }
    return count
}

/**
 * The first [max] code points of this string, never ending between the two halves of a surrogate
 * pair.
 *
 * @param max Most code points to keep; not negative.
 * @return This string when it is short enough, otherwise its leading [max] code points.
 */
internal fun String.takeCodePoints(max: Int): String {
    var count = 0
    var index = 0
    while (index < length && count < max) {
        index += if (isSurrogatePairAt(index)) 2 else 1
        count++
    }
    return substring(0, index)
}

/**
 * Whether a high surrogate at [index] is followed by its low surrogate.
 *
 * @param index Position to look at.
 * @return `true` when the characters at [index] and after it form one code point.
 */
private fun String.isSurrogatePairAt(index: Int): Boolean =
    this[index].isHighSurrogate() && index + 1 < length && this[index + 1].isLowSurrogate()
