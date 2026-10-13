package dev.voir.areca.foundation.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.LocalIconButtonSize
import dev.voir.areca.foundation.button.LocalIconButtonStyle
import dev.voir.areca.foundation.button.focusRing
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.modifier.clickCursor

/**
 * The container every field in this package is drawn inside.
 *
 * [Field] owns the box, the border, the state colors and the row of slots; it owns nothing about
 * the value itself. [TextField] passes it a text editor, [dev.voir.areca.foundation.field.select.SelectField]
 * passes it the label of the current selection, and a caller can pass it anything at all — the
 * container does not care.
 *
 * Colors are resolved into one [FieldVisualState] rather than blended, and animated between:
 * see [FieldStyle].
 * The container shape travels with the palette, in [FieldStyle.shape]. Sizes are published to the
 * children through [LocalTextStyle], [LocalIconSize], [LocalSpinnerSize], [LocalIconButtonSize]
 * and [LocalIconButtonStyle], so slots pick up the field's metrics without being told what size
 * they are in.
 *
 * [placeholder] is drawn over [content] rather than in place of it. That matters for an editor:
 * a text field whose inner editor were swapped out while empty would show no caret to type into.
 *
 * A label reaches the inside of the box through [field], which [FieldGroup] builds; a label
 * outside the box, and the message under it, belong to [FieldGroup] itself.
 *
 * @param modifier Modifier applied to the container.
 * @param style Color palette and shape. Defaults to the ambient [LocalFieldStyle].
 * @param size Typography, paddings and vertical metrics. Defaults to the ambient [LocalFieldSize].
 * @param field Scope handed down by an enclosing [FieldGroup]: the status to report, and a label
 *   to draw inside the container when the group placed it there. Null for a bare container.
 * @param status Validation outcome to report. Takes priority over focus when both apply. Comes
 *   from [field] unless it is given explicitly.
 * @param enabled When false, the field is non-interactive and drawn as disabled. Comes from
 *   [field] unless it is given explicitly.
 * @param readOnly The value can be read and selected but not changed.
 * @param loading Replaces the trailing slot with a spinner while a value is being fetched or
 *   validated. Does not disable the field.
 * @param empty Whether the field has no value, and so should show its [placeholder].
 * @param interactionSource Interaction source the container watches for focus and hover. Pass
 *   the same instance the inner control uses, or the border will not react to it. Null keeps one
 *   private to the container, which sees only the container's own hover, focus and presses.
 * @param onClick Makes the whole container clickable — for a field whose value is chosen
 *   elsewhere, such as a select or a date picker. Null leaves the container inert so that a
 *   control inside it handles its own clicks. A clickable container answers a press with a tint of
 *   its content color, following [FieldStyle.shape]; hover and focus are already drawn by the
 *   container's own state colors and ring, so the press is the only feedback added.
 * @param ownsControl Whether the container is the control itself rather than a frame around an
 *   editor, and so the node that carries [field]'s name, error and focus. Defaults to having an
 *   [onClick]; pass true for a container that is the control but cannot be clicked right now — a
 *   disabled, read-only or loading select, a display-only value — so it keeps its name.
 * @param leading Slot pinned to the start edge, drawn in the style's decoration color. Stays
 *   centered against the whole container, including a [FieldLabelPlacement.Inside] label.
 * @param trailing Slot pinned to the end edge, drawn in the style's decoration color. Hidden
 *   while [loading].
 * @param placeholder Hint drawn over the value slot while [empty].
 * @param content The value.
 */
@Composable
public fun Field(
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    empty: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
    ownsControl: Boolean = onClick != null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    val hasFocus by interactions.collectIsFocusedAsState()
    val hovered by interactions.collectIsHoveredAsState()

    val visualState = fieldVisualState(status, enabled, readOnly, hasFocus, hovered)

    // One color per role, mapped from the one state the field is in and animated between so a
    // field that gains focus or turns red does not snap. See [FieldVisualState] for the order.
    val colorSpec = fieldColorSpec()
    val backgroundColor by animateColorAsState(
        targetValue = resolveBackgroundColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldBg",
    )
    val borderColor by animateColorAsState(
        targetValue = resolveBorderColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldBorder",
    )
    val contentColor by animateColorAsState(
        targetValue = resolveContentColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldContent",
    )
    val placeholderColor by animateColorAsState(
        targetValue = resolvePlaceholderColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldPlaceholder",
    )
    val focusRingColor by animateColorAsState(
        targetValue = resolveFocusRingColor(style, visualState, hasFocus),
        animationSpec = colorSpec,
        label = "fieldFocusRing",
    )
    val labelColor by animateColorAsState(
        targetValue = style.label.colorFor(visualState),
        animationSpec = colorSpec,
        label = "fieldLabel",
    )

    val internalLabel = field.internalLabel()

    // The container owns the control's focus and semantics only when the container *is* the
    // control — a select, a date picker. An editor owns its own node and applies these there,
    // and attaching the same requester twice would make a label tap ambiguous. Focus is claimed
    // only while the clickable below can actually take it.
    val controlModifier = if (ownsControl) {
        Modifier.fieldControl(field, focusable = onClick != null && enabled)
    } else {
        Modifier
    }
    val pressIndication = remember(style.shape, style.content) {
        fieldPressIndication(shape = style.shape, ink = style.content)
    }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides size.textStyle,
        LocalIconSize provides size.icon,
        LocalSpinnerSize provides size.spinner,
        LocalIconButtonSize provides size.iconButton,
        LocalIconButtonStyle provides style.iconButton,
    ) {
        Box(
            modifier = modifier
                .heightIn(min = size.minHeight)
                .focusGroup() // group focus for children
                .fieldContainer(
                    style = style,
                    metrics = size,
                    bg = backgroundColor,
                    border = borderColor,
                    focusRing = focusRingColor,
                )
                // A clickable container reports its own hover; one wrapped around an editor does
                // not, and without this a text field would never light up under the pointer.
                .then(
                    if (onClick == null) {
                        Modifier.hoverable(interactions, enabled = enabled)
                    } else {
                        Modifier
                    },
                )
                .then(controlModifier)
                .then(
                    if (onClick != null) {
                        Modifier
                            .clickable(
                                enabled = enabled,
                                onClick = onClick,
                                interactionSource = interactions,
                                indication = pressIndication,
                            )
                            .clickCursor(enabled)
                    } else {
                        Modifier
                    },
                )
                .padding(size.padding),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leading != null) {
                    CompositionLocalProvider(
                        LocalContentColor provides style.extra,
                    ) {
                        leading()
                    }

                    Spacer(Modifier.width(size.gap))
                }

                when (val placement = internalLabel?.second) {
                    // Label and value share a line. The label keeps the full label type: at this
                    // size, shrinking one half of a two-column row reads as a mistake.
                    is FieldLabelPlacement.InsideLeading -> {
                        FieldInternalLabel(
                            label = internalLabel.first,
                            color = labelColor,
                            requiredColor = style.label.required,
                            textStyle = size.label.textStyle,
                            requiredGap = size.label.requiredGap,
                            modifier = if (placement.width.isSpecified) {
                                Modifier.width(placement.width)
                            } else {
                                Modifier
                            },
                        )

                        Spacer(Modifier.width(size.gap))

                        FieldValue(
                            modifier = Modifier.weight(1f),
                            empty = empty,
                            placeholderColor = placeholderColor,
                            placeholder = placeholder,
                            horizontalArrangement = when (placement.valueAlignment) {
                                Alignment.End -> Arrangement.End
                                Alignment.CenterHorizontally -> Arrangement.Center
                                else -> Arrangement.Start
                            },
                            content = content,
                        )
                    }

                    // Label stacked over the value. The column takes the weight so the leading
                    // and trailing slots stay centered against both lines.
                    FieldLabelPlacement.Inside -> Column(modifier = Modifier.weight(1f)) {
                        FieldInternalLabel(
                            label = internalLabel.first,
                            color = labelColor,
                            requiredColor = style.label.required,
                            textStyle = size.label.insideTextStyle,
                            requiredGap = size.label.requiredGap,
                        )

                        Spacer(Modifier.height(size.label.insideGap))

                        FieldValue(
                            modifier = Modifier.fillMaxWidth(),
                            empty = empty,
                            placeholderColor = placeholderColor,
                            placeholder = placeholder,
                            content = content,
                        )
                    }

                    null -> FieldValue(
                        modifier = Modifier.weight(1f),
                        empty = empty,
                        placeholderColor = placeholderColor,
                        placeholder = placeholder,
                        content = content,
                    )
                }

                when {
                    loading -> {
                        Spacer(Modifier.width(size.gap))
                        Spinner(style = SpinnerStyle(color = style.spinner))
                    }

                    trailing != null -> {
                        Spacer(Modifier.width(size.gap))
                        CompositionLocalProvider(
                            LocalContentColor provides style.extra,
                        ) {
                            trailing()
                        }
                    }
                }
            }
        }
    }
}

/**
 * The value slot, with the placeholder layered behind it.
 *
 * @param modifier Modifier applied to the slot. Carries the weight or the width that decides how
 *   much of the row the value gets.
 * @param empty Whether the field has no value, and so should show its placeholder.
 * @param placeholderColor Resolved placeholder foreground.
 * @param placeholder Hint drawn over the value while [empty], or null for a field with none.
 * @param horizontalArrangement Which edge the value sits against. Only visible for a value
 *   narrower than the slot.
 * @param content The value.
 */
@Composable
private fun FieldValue(
    modifier: Modifier,
    empty: Boolean,
    placeholderColor: Color,
    placeholder: (@Composable RowScope.() -> Unit)?,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {
    // Top-aligned, not centered. The box is as tall as whatever is in it, and in a multi-line
    // editor that is several lines — a centered placeholder then sits on the middle line of an
    // empty field, below a caret blinking on the first. At one line the two alignments are the
    // same, so this changes nothing for the fields that are not multi-line. The field as a whole
    // still centers itself inside its minimum height; that happens in the container above.
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart,
    ) {
        if (placeholder != null) {
            FieldPlaceholder(
                visible = empty,
                color = placeholderColor,
                horizontalArrangement = horizontalArrangement,
                content = placeholder,
            )
        }

        // Always composed, and drawn above the placeholder. A text editor swapped out while
        // empty takes its caret and its selection with it.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.Top,
            content = content,
        )
    }
}

/**
 * A label drawn inside the container, in the color the container resolved for it.
 *
 * @param label Text and required marker to draw.
 * @param color Resolved label foreground.
 * @param requiredColor Foreground of the required marker.
 * @param textStyle Type for the label and its marker.
 * @param requiredGap Space between the label and the marker.
 * @param modifier Modifier applied to the label row, carrying a fixed width where one applies.
 */
@Composable
private fun FieldInternalLabel(
    label: FieldLabel,
    color: Color,
    requiredColor: Color,
    textStyle: TextStyle,
    requiredGap: Dp,
    modifier: Modifier = Modifier,
) {
    FieldGroupDefaults.Label(
        text = label.text,
        modifier = modifier,
        required = label.required,
        requiredText = label.requiredText,
        color = color,
        requiredColor = requiredColor,
        textStyle = textStyle,
        requiredGap = requiredGap,
    )
}

/**
 * The placeholder layer, faded in and out behind the value.
 *
 * @param visible Whether the field is empty and the hint applies.
 * @param color Resolved placeholder foreground.
 * @param horizontalArrangement Which edge the hint sits against, matching the value it stands in
 *   for.
 * @param content The hint.
 */
@Composable
private fun FieldPlaceholder(
    visible: Boolean,
    color: Color,
    horizontalArrangement: Arrangement.Horizontal,
    content: @Composable RowScope.() -> Unit,
) {
    val placeholderAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = fieldFadeSpec(),
        label = "fieldPlaceholderAlpha",
    )

    if (placeholderAlpha > 0f) {
        CompositionLocalProvider(LocalContentColor provides color) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(placeholderAlpha),
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}
