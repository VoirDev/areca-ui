package dev.voir.areca.foundation.field

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.modifier.clickCursor

/**
 * How a [FieldGroup] makes room for the single line of text below the control.
 *
 * The choice is a trade between two costs: reserving space taxes every field on the form for a
 * message most of them never show, while not reserving it moves whatever sits below the field
 * when a message arrives. [Animated] makes that movement read as the form answering rather than
 * as the page jumping, which is why it is the default.
 */
@Immutable
public sealed interface FieldMessageBehavior {
    /**
     * No space is reserved. The message expands and fades in when it appears, and shrinks and
     * fades out when it goes away.
     */
    public data object Animated : FieldMessageBehavior

    /**
     * No space is reserved and no transition runs. The message appears and disappears with the
     * frame that changed it. Use when the group sits inside something that animates its own
     * height and a nested transition would fight it.
     */
    public data object Immediate : FieldMessageBehavior

    /**
     * Space for the message is always held, so nothing below the field ever moves.
     *
     * @property minHeight Height held below the control even when there is no message. A message
     *   taller than this grows the slot rather than being clipped. Unspecified holds
     *   [FieldMessageMetrics.reservedHeight] from the group's size.
     */
    public data class Reserve(public val minHeight: Dp = Dp.Unspecified) : FieldMessageBehavior
}

/**
 * Stock text renderers for [FieldGroup].
 *
 * The renderers are exposed so a caller who needs a different label — an icon beside it, a
 * tooltip trigger, a character counter opposite it — can compose one and pass it to
 * [FieldGroupLayout] instead of reimplementing the type ramp. Their colors and type come from
 * [FieldStyle] and [FieldSize], so overriding those reaches these renderers too.
 */
public object FieldGroupDefaults {
    /**
     * Label describing the control, with an optional required marker after it.
     *
     * The marker is a separate [Text] rather than text appended to [text] so it keeps its own
     * color when the label is translated, and so a label that already ends in punctuation does
     * not have to be edited to accommodate it.
     *
     * @param text Label content.
     * @param modifier Modifier applied to the label row.
     * @param required Whether to show the required marker.
     * @param requiredText Marker shown when [required] is true.
     * @param color Label foreground color. [FieldLabelStyle] holds one per state.
     * @param requiredColor Marker foreground color.
     * @param textStyle Type applied to both the label and the marker.
     * @param requiredGap Space between the label and the marker.
     * @param verticalAlignment Alignment of the marker against the label.
     */
    @Composable
    public fun Label(
        text: CharSequence,
        modifier: Modifier = Modifier,
        required: Boolean = false,
        requiredText: String = "*",
        color: Color = FieldDefaults.style.label.content,
        requiredColor: Color = FieldDefaults.style.label.required,
        textStyle: TextStyle = FieldDefaults.size.label.textStyle,
        requiredGap: Dp = FieldDefaults.size.label.requiredGap,
        verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalTextStyle provides textStyle) {
            Row(
                modifier = modifier,
                verticalAlignment = verticalAlignment,
            ) {
                Text(
                    text = text,
                    color = color,
                )
                if (required) {
                    Spacer(Modifier.width(requiredGap))
                    Text(
                        text = requiredText,
                        color = requiredColor,
                    )
                }
            }
        }
    }

    /**
     * One line of text below the control.
     *
     * @param text Message content.
     * @param modifier Modifier applied to the message.
     * @param color Message foreground color. [FieldMessageStyle] holds one per state.
     * @param textStyle Type applied to the message.
     */
    @Composable
    public fun Message(
        text: CharSequence,
        modifier: Modifier = Modifier,
        color: Color = FieldDefaults.style.message.content,
        textStyle: TextStyle = FieldDefaults.size.message.textStyle,
    ) {
        CompositionLocalProvider(LocalTextStyle provides textStyle) {
            Text(
                text = text,
                modifier = modifier,
                color = color,
            )
        }
    }

    /**
     * [Message] in the danger color: something is wrong with the current value.
     *
     * @param text Message content.
     * @param modifier Modifier applied to the message.
     * @param color Message foreground color.
     * @param textStyle Type applied to the message.
     */
    @Composable
    public fun Error(
        text: CharSequence,
        modifier: Modifier = Modifier,
        color: Color = FieldDefaults.style.message.contentError,
        textStyle: TextStyle = FieldDefaults.size.message.textStyle,
    ) {
        Message(text = text, modifier = modifier, color = color, textStyle = textStyle)
    }

    /**
     * [Message] in the muted color: a hint about what the control expects, shown while nothing
     * is wrong.
     *
     * @param text Message content.
     * @param modifier Modifier applied to the message.
     * @param color Message foreground color.
     * @param textStyle Type applied to the message.
     */
    @Composable
    public fun Assistive(
        text: CharSequence,
        modifier: Modifier = Modifier,
        color: Color = FieldDefaults.style.message.content,
        textStyle: TextStyle = FieldDefaults.size.message.textStyle,
    ) {
        Message(text = text, modifier = modifier, color = color, textStyle = textStyle)
    }
}

/**
 * A control with a label and one line of explanation, wherever those two belong.
 *
 * The group owns the field's [FieldStatus] and hands it to [content] inside a [FieldScope], so
 * the message and the control cannot disagree about whether something is wrong: an [error] with
 * a field still drawn in its resting border is not a state this API can express.
 *
 * [labelPlacement] decides who draws the label. Under a [FieldLabelPlacement.External] placement
 * the group draws it above or beside the control. Under a [FieldLabelPlacement.Internal] one the
 * group draws nothing and puts the label in the scope instead, for [Field] to draw inside the
 * container — which is why the scope has to reach the control:
 *
 * ```kotlin
 * FieldGroup(
 *     label = "Email",
 *     required = true,
 *     error = emailError,
 *     labelPlacement = FieldLabelPlacement.Inside,
 * ) { field ->
 *     TextField(
 *         value = email,
 *         onValueChange = { email = it },
 *         modifier = Modifier.fillMaxWidth(),
 *         field = field,
 *         singleLine = true,
 *         placeholder = { Text("you@example.com") },
 *     )
 * }
 * ```
 *
 * Moving that label above the field is one argument, and nothing else about the call changes.
 *
 * Only one message shows at a time. [error] wins over [assistive], because a hint about what to
 * type is of no use to somebody being told what they typed is wrong. By default no space is
 * reserved for it — see [FieldMessageBehavior]. The text of a departing message is held until
 * its exit transition finishes, so it fades out rather than blanking halfway through. An [error]
 * shown below the control is announced to assistive technology when it appears and whenever it
 * changes, through [AnnounceOnChange].
 *
 * The external label follows the control it names: drawn in the focused color while the control
 * holds focus, which the group learns through [FieldScope.focus], and dimmed while the group is
 * not [enabled].
 *
 * @param modifier Modifier applied to the whole group.
 * @param label Text describing the control, or null for a group that carries no label.
 * @param required Whether the label shows a required marker.
 * @param requiredText Marker shown after the label when [required] is true.
 * @param requiredDescription How the required marker is read aloud. The marker itself is a
 *   glyph a screen reader would either skip or spell out, so the control announces this
 *   word instead.
 * @param assistive Hint shown below the control while [error] is null.
 * @param error Problem with the current value, or null when there is nothing to report.
 *   Its presence is what drives [status], and therefore how the control is drawn.
 * @param status Status handed to [content] and used to color [error]. Defaults to
 *   [FieldStatus.Error] whenever [error] is set; pass it explicitly to report a warning or a
 *   success alongside a message.
 * @param style Colors for the label and the message. Defaults to the ambient [LocalFieldStyle],
 *   so a group and the field inside it agree without being told twice.
 * @param size Type and spacing for the label and the message. Defaults to the ambient
 *   [LocalFieldSize].
 * @param labelPlacement Where the label goes. An internal placement only takes effect if
 *   [content] forwards its scope to the control.
 * @param messagePlacement Whether the assistive or error line is drawn at all.
 * @param messageBehavior How room is made for the message line.
 * @param labelSpace Gap between an external label and the control.
 * @param messageSpace Gap between the control and the message.
 * @param enabled Whether the control accepts input. Handed to [content] in the scope, so a field
 *   that forwards it is disabled along with its label and message; a disabled group's label no
 *   longer moves focus when tapped.
 * @param content The control, given the scope the group resolved for it.
 */
@Composable
public fun FieldGroup(
    modifier: Modifier = Modifier,
    label: CharSequence? = null,
    required: Boolean = false,
    requiredText: String = "*",
    requiredDescription: String = "required",
    assistive: CharSequence? = null,
    error: CharSequence? = null,
    status: FieldStatus = if (error != null) FieldStatus.Error else FieldStatus.Default,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    labelPlacement: FieldLabelPlacement = FieldLabelPlacement.Above,
    messagePlacement: FieldMessagePlacement = FieldMessagePlacement.Below,
    messageBehavior: FieldMessageBehavior = FieldMessageBehavior.Animated,
    labelSpace: Dp = size.label.gap,
    messageSpace: Dp = size.message.gap,
    enabled: Boolean = true,
    content: @Composable (field: FieldScope) -> Unit,
) {
    val focus = remember { FieldFocus() }
    val scope = fieldGroupScope(
        label = label,
        required = required,
        requiredText = requiredText,
        requiredDescription = requiredDescription,
        error = error,
        status = status,
        labelPlacement = labelPlacement,
        enabled = enabled,
        focus = focus,
    )
    val fieldLabel = scope.label

    // The label follows the control: focused while the control holds focus, dimmed while it is
    // disabled, and in the status color whenever there is one to report.
    val visualState = fieldVisualState(
        status = status,
        enabled = enabled,
        focused = focus.focused,
    )
    val colorSpec = fieldColorSpec()
    val labelColor by animateColorAsState(
        targetValue = style.label.colorFor(visualState),
        animationSpec = colorSpec,
        label = "fieldGroupLabel",
    )

    // Exactly one of the two draws the label: the container when the placement is internal,
    // this group otherwise.
    val labelSlot: (@Composable () -> Unit)? =
        if (labelPlacement !is FieldLabelPlacement.Internal && fieldLabel != null) {
            @Composable {
                FieldGroupDefaults.Label(
                    text = fieldLabel.text,
                    // A tap on the label belongs to the control, the way `<label for>` behaves.
                    // pointerInput rather than clickable: a label is not a button, and should
                    // neither take a tab stop nor announce itself as one.
                    modifier = if (enabled) {
                        Modifier
                            .pointerInput(focus) { detectTapGestures { focus.request() } }
                            .clickCursor(enabled = true)
                    } else {
                        Modifier
                    },
                    required = fieldLabel.required,
                    requiredText = fieldLabel.requiredText,
                    color = labelColor,
                    requiredColor = style.label.required,
                    textStyle = size.label.textStyle,
                    requiredGap = size.label.requiredGap,
                )
            }
        } else {
            null
        }

    // An assistive hint keeps the resting color while the field is usable: it describes what to
    // type, not the state the value is in.
    val messageColor = if (error != null || !enabled) {
        style.message.colorFor(visualState)
    } else {
        style.message.content
    }
    val shownError = error?.takeIf { messagePlacement == FieldMessagePlacement.Below }
    val messageText = shownError
        ?: assistive?.takeIf { messagePlacement == FieldMessagePlacement.Below }

    // An error is announced because it is news; an assistive hint is not, and a screen reader
    // repeating it on every keystroke would bury the field it belongs to. Announced rather than
    // marked as a live region, because the message node is created with its text and a live
    // region is silent on the frame it first appears.
    AnnounceOnChange(shownError?.toString())

    val messageSlot: (@Composable () -> Unit)? = messageText?.let { text ->
        @Composable {
            FieldGroupDefaults.Message(
                text = text,
                color = messageColor,
                textStyle = size.message.textStyle,
            )
        }
    }

    FieldGroupLayout(
        modifier = modifier,
        label = labelSlot,
        message = messageSlot,
        size = size,
        labelPlacement = labelPlacement as? FieldLabelPlacement.External
            ?: FieldLabelPlacement.None,
        messageBehavior = messageBehavior,
        labelSpace = labelSpace,
        messageSpace = messageSpace,
    ) {
        content(scope)
    }
}

/**
 * Resolves the [FieldScope] a [FieldGroup] hands to its control.
 *
 * Pure, so the rules for what the control announces can be tested without composing anything.
 *
 * @param label Text describing the control, or null for a group that carries no label.
 * @param required Whether the label shows a required marker.
 * @param requiredText Marker shown after the label when [required] is true.
 * @param requiredDescription How the required marker is read aloud.
 * @param error Problem with the current value, or null when there is nothing to report.
 * @param status Status the group reports.
 * @param labelPlacement Where the label goes.
 * @param enabled Whether the control accepts input.
 * @param focus Handle the control claims for focus, or null for none.
 * @return The scope to hand to the control.
 */
internal fun fieldGroupScope(
    label: CharSequence?,
    required: Boolean,
    requiredText: String,
    requiredDescription: String,
    error: CharSequence?,
    status: FieldStatus,
    labelPlacement: FieldLabelPlacement,
    enabled: Boolean,
    focus: FieldFocus?,
): FieldScope {
    val fieldLabel = label?.let {
        FieldLabel(text = it, required = required, requiredText = requiredText)
    }

    // An external label is a sibling of the control, so the control has to name itself. An
    // internal one is already a descendant of the control's semantics node, where naming it
    // again would be read in place of the value rather than alongside it.
    val description = fieldLabel
        ?.takeIf { labelPlacement !is FieldLabelPlacement.Internal }
        ?.let { if (it.required) "${it.text}, $requiredDescription" else it.text.toString() }

    return FieldScope(
        status = status,
        label = fieldLabel,
        labelPlacement = labelPlacement,
        contentDescription = description,
        // Only an error makes a control invalid. A warning or a confirmation is worth saying
        // and is not worth telling the platform the value cannot be submitted.
        errorDescription = error?.takeIf { status == FieldStatus.Error },
        focus = focus,
        enabled = enabled,
    )
}

/**
 * The arrangement behind [FieldGroup], with the label and message as slots.
 *
 * Use this when the stock renderers do not fit — a label with a counter opposite it, a message
 * carrying an icon, a control whose status comes from somewhere other than an error string.
 * [FieldGroup] is the version to reach for otherwise: it also resolves the status, which this
 * one leaves to the caller.
 *
 * Only [FieldLabelPlacement.External] placements are accepted. This layout wraps a control it
 * cannot see into, so it has nowhere to put a label that belongs inside the container; that
 * placement needs the scope [FieldGroup] builds.
 *
 * @param modifier Modifier applied to the whole group.
 * @param label Label slot, or null for a group that carries no label.
 * @param message Message slot, or null when there is nothing to say. Whether the slot is null is
 *   what drives the transition under [FieldMessageBehavior.Animated]; the last non-null slot is
 *   held and re-invoked until that transition finishes, so a message fades out with its text
 *   still on it.
 * @param size Spacing for the label and the message, and the height
 *   [FieldMessageBehavior.Reserve] holds when it names none. Defaults to the ambient
 *   [LocalFieldSize].
 * @param labelPlacement Whether the label sits above the control, beside it, or nowhere.
 * @param messageBehavior How room is made for the message line.
 * @param labelSpace Gap between the label and the control.
 * @param messageSpace Gap between the control and the message.
 * @param content The control being described.
 */
@Composable
public fun FieldGroupLayout(
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    message: (@Composable () -> Unit)? = null,
    size: FieldSize = FieldDefaults.size,
    labelPlacement: FieldLabelPlacement.External = FieldLabelPlacement.Above,
    messageBehavior: FieldMessageBehavior = FieldMessageBehavior.Animated,
    labelSpace: Dp = size.label.gap,
    messageSpace: Dp = size.message.gap,
    content: @Composable () -> Unit,
) {
    when (labelPlacement) {
        FieldLabelPlacement.None, FieldLabelPlacement.Above -> Column(modifier = modifier) {
            if (label != null && labelPlacement == FieldLabelPlacement.Above) {
                label()
                Spacer(Modifier.height(labelSpace))
            }

            content()

            FieldGroupMessageSlot(
                message = message,
                behavior = messageBehavior,
                space = messageSpace,
                reservedHeight = size.message.reservedHeight,
            )
        }

        is FieldLabelPlacement.Beside -> Column(modifier = modifier) {
            Row(verticalAlignment = labelPlacement.alignment) {
                if (label != null) {
                    Box(
                        modifier = if (labelPlacement.width.isSpecified) {
                            Modifier.width(labelPlacement.width)
                        } else {
                            Modifier
                        },
                    ) {
                        label()
                    }

                    Spacer(Modifier.width(labelSpace))
                }

                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
            }

            FieldGroupMessageSlot(
                message = message,
                behavior = messageBehavior,
                space = messageSpace,
                reservedHeight = size.message.reservedHeight,
                // Line the message up under the control rather than under the label, but only
                // when the label column has a width we can count on.
                modifier = if (label != null && labelPlacement.width.isSpecified) {
                    Modifier.padding(start = labelPlacement.width + labelSpace)
                } else {
                    Modifier
                },
            )
        }
    }
}

/**
 * Holds the last message a group rendered.
 *
 * A plain field rather than snapshot state on purpose: the slot is a composable lambda, whose
 * identity changes on every recomposition when its captures are unstable. Writing that to a
 * [androidx.compose.runtime.MutableState] read in the same scope would invalidate the scope that
 * just wrote it, and the group would recompose forever.
 */
private class FieldGroupMessageHolder {
    /** Last non-null message slot, or null before the group has ever shown one. */
    var content: (@Composable () -> Unit)? = null
}

/**
 * Renders the message below the control according to [behavior].
 *
 * @param message Message slot, or null when there is nothing to say.
 * @param behavior How room is made for the message.
 * @param space Gap between the control and the message.
 * @param reservedHeight Height held under [FieldMessageBehavior.Reserve] when it names none.
 * @param modifier Modifier applied to the message itself, not to the space above it.
 */
@Composable
private fun FieldGroupMessageSlot(
    message: (@Composable () -> Unit)?,
    behavior: FieldMessageBehavior,
    space: Dp,
    reservedHeight: Dp,
    modifier: Modifier = Modifier,
) {
    // A departing message is still on screen for the length of its exit transition, by which
    // point the caller has already stopped passing it. Keep the last one so it fades out rather
    // than blanking halfway through.
    val holder = remember { FieldGroupMessageHolder() }
    if (message != null) holder.content = message

    when (behavior) {
        FieldMessageBehavior.Animated -> AnimatedVisibility(
            visible = message != null,
            enter = fieldMessageEnter(),
            exit = fieldMessageExit(),
        ) {
            Column(modifier = modifier) {
                Spacer(Modifier.height(space))
                // Reading `message` here is what re-composes this content when it changes; the
                // holder only stands in while the exit transition plays it out.
                (message ?: holder.content)?.invoke()
            }
        }

        FieldMessageBehavior.Immediate -> if (message != null) {
            Spacer(Modifier.height(space))
            Box(modifier = modifier) {
                message()
            }
        }

        is FieldMessageBehavior.Reserve -> {
            Spacer(Modifier.height(space))
            // heightIn rather than height: a message that wraps to two lines grows the slot
            // instead of being clipped by the room we guessed it would need.
            val held = if (behavior.minHeight.isSpecified) behavior.minHeight else reservedHeight
            Box(modifier = modifier.heightIn(min = held)) {
                message?.invoke()
            }
        }
    }
}
