package dev.voir.areca.foundation.field

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Which edges of the container carry the border.
 *
 * Part of the palette rather than a parameter on [Field] because it is what separates one style
 * from another: an underlined field is not an outlined field with a setting changed, it is a
 * different look, and the two are never mixed within a form.
 */
public enum class FieldBorderPlacement {
    /** Every edge. The box a border usually means. */
    All,

    /** The bottom edge only — a rule under the value rather than a box around it. */
    Bottom,
}

/**
 * Colors for the label that describes a field.
 *
 * Every state override defaults to [content], so a label that does not change with the field is
 * a two-argument construction. Which one applies is decided by [FieldVisualState], the same way
 * it is for the box beneath it.
 *
 * @property content Label foreground at rest.
 * @property contentDisabled Label foreground when the field rejects input entirely.
 * @property contentReadOnly Label foreground when the value cannot be changed.
 * @property contentFocused Label foreground while the field holds focus.
 * @property contentError Label foreground under [FieldStatus.Error].
 * @property contentWarning Label foreground under [FieldStatus.Warning].
 * @property contentSuccess Label foreground under [FieldStatus.Success].
 * @property required Foreground of the marker that says the value cannot be left out.
 */
@Stable
public data class FieldLabelStyle(
    public val content: Color,
    public val contentDisabled: Color = content,
    public val contentReadOnly: Color = content,
    public val contentFocused: Color = content,
    public val contentError: Color = content,
    public val contentWarning: Color = content,
    public val contentSuccess: Color = content,
    public val required: Color,
)

/**
 * Colors for the single line of text that explains a field.
 *
 * The same object covers the assistive hint and the validation message: which color applies is
 * decided by the [FieldStatus] being reported, not by which of the two strings was passed.
 *
 * @property content Message foreground when there is nothing to report.
 * @property contentDisabled Message foreground when the field rejects input entirely.
 * @property contentError Message foreground under [FieldStatus.Error].
 * @property contentWarning Message foreground under [FieldStatus.Warning].
 * @property contentSuccess Message foreground under [FieldStatus.Success].
 */
@Stable
public data class FieldMessageStyle(
    public val content: Color,
    public val contentDisabled: Color = content,
    public val contentError: Color = content,
    public val contentWarning: Color = content,
    public val contentSuccess: Color = content,
)

/**
 * Colors and shape for every state a [Field] can be in, label and message included.
 *
 * Each visual role — background, border, content, placeholder — carries one base color plus an
 * override per state. Every override defaults to the base, so a flat style names the four base
 * colors plus the parts that have nothing to fall back on — [extra], [spinner], [shape], [label],
 * [message] and [iconButton] — and only the states a style actually distinguishes need naming.
 *
 * States do not blend. Everything that might apply is resolved into one [FieldVisualState],
 * and every role maps from that, so the container, the value, the label and the message cannot
 * disagree about which state the field is in.
 *
 * @property bg Container background at rest.
 * @property bgDisabled Background when the field rejects input entirely.
 * @property bgReadOnly Background when the value can be read and selected but not changed.
 * @property bgHovered Background while the pointer is over the field and nothing else applies.
 * @property bgFocused Background while the field holds focus.
 * @property bgError Background under [FieldStatus.Error].
 * @property bgSuccess Background under [FieldStatus.Success].
 * @property bgWarning Background under [FieldStatus.Warning].
 * @property border Container border at rest.
 * @property borderDisabled Border when the field rejects input entirely.
 * @property borderReadOnly Border when the value cannot be changed.
 * @property borderHovered Border while the pointer is over the field.
 * @property borderFocused Border while the field holds focus.
 * @property borderError Border under [FieldStatus.Error].
 * @property borderSuccess Border under [FieldStatus.Success].
 * @property borderWarning Border under [FieldStatus.Warning].
 * @property borderPlacement Which edges the border is drawn on.
 * @property content Foreground of the value at rest.
 * @property contentDisabled Value foreground when the field rejects input entirely.
 * @property contentReadOnly Value foreground when the value cannot be changed.
 * @property contentHovered Value foreground while the pointer is over the field.
 * @property contentFocused Value foreground while the field holds focus.
 * @property contentError Value foreground under [FieldStatus.Error].
 * @property contentWarning Value foreground under [FieldStatus.Warning].
 * @property contentSuccess Value foreground under [FieldStatus.Success].
 * @property placeholder Placeholder foreground at rest.
 * @property placeholderDisabled Placeholder foreground when the field rejects input entirely.
 * @property placeholderReadOnly Placeholder foreground when the value cannot be changed.
 * @property placeholderHovered Placeholder foreground while the pointer is over the field.
 * @property placeholderFocused Placeholder foreground while the field holds focus.
 * @property placeholderError Placeholder foreground under [FieldStatus.Error].
 * @property placeholderWarning Placeholder foreground under [FieldStatus.Warning].
 * @property placeholderSuccess Placeholder foreground under [FieldStatus.Success].
 * @property extra Foreground for the leading and trailing slots and for affixes — decoration
 *   that belongs to the field rather than to the value, and is held back from it.
 * @property divider Rule between the segments of a [FieldRow]. Ignored by a single-part field.
 * @property spinner Color of the loading indicator.
 * @property focusRing Halo drawn just outside the container while the field holds focus, in
 *   the space [FieldSize.focusRing] reserves for it. [Color.Transparent] — the default — draws
 *   nothing, which is what every style that predates the ring keeps doing.
 * @property focusRingError Ring color while a focused field is reporting [FieldStatus.Error].
 * @property focusRingWarning Ring color while a focused field is reporting [FieldStatus.Warning].
 * @property focusRingSuccess Ring color while a focused field is reporting [FieldStatus.Success].
 * @property shape Container shape, used for the background, the clip, the border and the press
 *   tint of a clickable field alike.
 * @property label Colors for the label describing the field.
 * @property message Colors for the assistive or validation line.
 * @property iconButton Style handed down to any [dev.voir.areca.foundation.button.IconButton] inside
 *   the field, so a clear or reveal control inherits the field's palette rather than the
 *   ambient one.
 */
@Stable
public data class FieldStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val bgReadOnly: Color = bg,
    public val bgHovered: Color = bg,
    public val bgFocused: Color = bg,
    public val bgError: Color = bg,
    public val bgSuccess: Color = bg,
    public val bgWarning: Color = bg,
    public val border: Color,
    public val borderDisabled: Color = border,
    public val borderReadOnly: Color = border,
    public val borderHovered: Color = border,
    public val borderFocused: Color = border,
    public val borderError: Color = border,
    public val borderSuccess: Color = border,
    public val borderWarning: Color = border,
    public val borderPlacement: FieldBorderPlacement = FieldBorderPlacement.All,
    public val content: Color,
    public val contentDisabled: Color = content,
    public val contentReadOnly: Color = content,
    public val contentHovered: Color = content,
    public val contentFocused: Color = content,
    public val contentError: Color = content,
    public val contentWarning: Color = content,
    public val contentSuccess: Color = content,
    public val placeholder: Color,
    public val placeholderDisabled: Color = placeholder,
    public val placeholderReadOnly: Color = placeholder,
    public val placeholderHovered: Color = placeholder,
    public val placeholderFocused: Color = placeholder,
    public val placeholderError: Color = placeholder,
    public val placeholderWarning: Color = placeholder,
    public val placeholderSuccess: Color = placeholder,
    public val extra: Color,
    public val divider: Color = border,
    public val spinner: Color,
    public val focusRing: Color = Color.Transparent,
    public val focusRingError: Color = focusRing,
    public val focusRingWarning: Color = focusRing,
    public val focusRingSuccess: Color = focusRing,
    public val shape: Shape,
    public val label: FieldLabelStyle,
    public val message: FieldMessageStyle,
    public val iconButton: IconButtonStyle,
) {
    /** The stock field styles, and the label and message palettes they share. */
    public companion object {
        /**
         * The label palette every stock style shares: a muted label with an accented marker.
         *
         * @return Label colors that do not change with the field's state.
         */
        @Composable
        public fun defaultLabel(): FieldLabelStyle = FieldLabelStyle(
            content = ArecaTheme.colors.fgSubtle,
            required = ArecaTheme.colors.accentBase,
        )

        /**
         * The message palette every stock style shares: muted at rest, and the matching
         * semantic color for each status.
         *
         * @return Message colors keyed to [FieldStatus].
         */
        @Composable
        public fun defaultMessage(): FieldMessageStyle = FieldMessageStyle(
            content = ArecaTheme.colors.fgMuted,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            contentError = ArecaTheme.colors.fgDangerText,
            contentWarning = ArecaTheme.colors.fgWarnText,
            contentSuccess = ArecaTheme.colors.fgSuccessText,
        )

        /**
         * No container at all: the value sits directly on whatever is behind it.
         *
         * For fields inside a surface that already draws the boundary — a table cell, a list row —
         * where a second border would be a box inside a box.
         *
         * @param shape Container shape. Irrelevant to the drawing while the background and
         *   border are transparent, but it still clips the content.
         * @return Style with a transparent background and border in every state.
         */
        @Composable
        public fun inline(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): FieldStyle = FieldStyle(
            bg = Color.Transparent,
            border = Color.Transparent,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            divider = ArecaTheme.colors.borderSubtle,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )

        /**
         * The default: a canvas-colored box behind a one-pixel border that carries the state.
         *
         * @param shape Container shape.
         * @return Style whose border reports focus and status, and whose background reports only
         *   whether the field can be edited.
         */
        @Composable
        public fun outlined(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): FieldStyle = FieldStyle(
            bg = ArecaTheme.colors.bgCanvas,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            bgReadOnly = ArecaTheme.colors.bgDisabled,
            border = ArecaTheme.colors.borderBase,
            borderDisabled = ArecaTheme.colors.borderDisabled,
            borderReadOnly = ArecaTheme.colors.borderDisabled,
            borderHovered = ArecaTheme.colors.borderStrong,
            borderFocused = ArecaTheme.colors.accentBase,
            borderError = ArecaTheme.colors.borderDanger,
            borderSuccess = ArecaTheme.colors.borderSuccess,
            borderWarning = ArecaTheme.colors.borderWarn,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            divider = ArecaTheme.colors.borderBase,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            focusRing = ArecaTheme.colors.accentBase.copy(alpha = FocusRingAlpha),
            focusRingError = ArecaTheme.colors.borderDanger.copy(alpha = FocusRingAlpha),
            focusRingWarning = ArecaTheme.colors.borderWarn.copy(alpha = FocusRingAlpha),
            focusRingSuccess = ArecaTheme.colors.borderSuccess.copy(alpha = FocusRingAlpha),
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )

        /**
         * A tinted fill with no border at rest, which grows an accented one on focus.
         *
         * The quiet style for a long form: a column of these reads as a list of slots rather than
         * as a stack of boxes, because nothing is outlined until you are in it. Unlike
         * [solidNeutral] it carries the full status palette — an error tints the whole fill, so
         * the field can report a problem without a message under it.
         *
         * @param shape Container shape.
         * @return Style with a neutral fill whose tint carries the status.
         */
        @Composable
        public fun soft(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): FieldStyle = FieldStyle(
            bg = ArecaTheme.colors.bgSubtle,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            bgReadOnly = ArecaTheme.colors.bgDisabled,
            bgHovered = ArecaTheme.colors.bgMuted,
            bgFocused = ArecaTheme.colors.bgSubtle,
            bgError = ArecaTheme.colors.bgDangerMuted,
            bgWarning = ArecaTheme.colors.bgWarnMuted,
            bgSuccess = ArecaTheme.colors.bgSuccessMuted,
            border = Color.Transparent,
            borderFocused = ArecaTheme.colors.accentBase,
            borderError = ArecaTheme.colors.borderDanger,
            borderWarning = ArecaTheme.colors.borderWarn,
            borderSuccess = ArecaTheme.colors.borderSuccess,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            divider = ArecaTheme.colors.borderBase,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            focusRing = ArecaTheme.colors.accentBase.copy(alpha = FocusRingAlpha),
            focusRingError = ArecaTheme.colors.borderDanger.copy(alpha = FocusRingAlpha),
            focusRingWarning = ArecaTheme.colors.borderWarn.copy(alpha = FocusRingAlpha),
            focusRingSuccess = ArecaTheme.colors.borderSuccess.copy(alpha = FocusRingAlpha),
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )

        /**
         * A tinted fill *and* a border, both of which carry the state.
         *
         * The loudest of the stock styles, and the one to reach for when fields sit on a busy or
         * colored surface where [outlined]'s canvas fill would disappear into the background.
         *
         * @param shape Container shape.
         * @return Style whose fill and border move together through every state.
         */
        @Composable
        public fun filled(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): FieldStyle = FieldStyle(
            bg = ArecaTheme.colors.bgSubtle,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            bgReadOnly = ArecaTheme.colors.bgDisabled,
            bgHovered = ArecaTheme.colors.bgMuted,
            bgFocused = ArecaTheme.colors.bgCanvas,
            bgError = ArecaTheme.colors.bgDangerMuted,
            bgWarning = ArecaTheme.colors.bgWarnMuted,
            bgSuccess = ArecaTheme.colors.bgSuccessMuted,
            border = ArecaTheme.colors.borderBase,
            borderDisabled = ArecaTheme.colors.borderDisabled,
            borderReadOnly = ArecaTheme.colors.borderDisabled,
            borderHovered = ArecaTheme.colors.borderStrong,
            borderFocused = ArecaTheme.colors.accentBase,
            borderError = ArecaTheme.colors.borderDanger,
            borderWarning = ArecaTheme.colors.borderWarn,
            borderSuccess = ArecaTheme.colors.borderSuccess,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            divider = ArecaTheme.colors.borderBase,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            focusRing = ArecaTheme.colors.accentBase.copy(alpha = FocusRingAlpha),
            focusRingError = ArecaTheme.colors.borderDanger.copy(alpha = FocusRingAlpha),
            focusRingWarning = ArecaTheme.colors.borderWarn.copy(alpha = FocusRingAlpha),
            focusRingSuccess = ArecaTheme.colors.borderSuccess.copy(alpha = FocusRingAlpha),
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )

        /**
         * A rule under the value and nothing else.
         *
         * The lightest style that still separates one field from the next, for dense settings
         * screens and for forms laid over an image or a colored panel. It draws no fill, so it
         * takes whatever is behind it; it also has no focus ring, because a ring around a box
         * that was never drawn reads as a box appearing.
         *
         * @param shape Container shape. Still clips the content, and rounds nothing visible while
         *   only the bottom edge is drawn.
         * @return Style with a bottom border that carries focus and status.
         */
        @Composable
        public fun underlined(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.none),
        ): FieldStyle = FieldStyle(
            bg = Color.Transparent,
            border = ArecaTheme.colors.borderBase,
            borderDisabled = ArecaTheme.colors.borderDisabled,
            borderReadOnly = ArecaTheme.colors.borderDisabled,
            borderHovered = ArecaTheme.colors.borderStrong,
            borderFocused = ArecaTheme.colors.accentBase,
            borderError = ArecaTheme.colors.borderDanger,
            borderSuccess = ArecaTheme.colors.borderSuccess,
            borderWarning = ArecaTheme.colors.borderWarn,
            borderPlacement = FieldBorderPlacement.Bottom,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            divider = ArecaTheme.colors.borderBase,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )

        /**
         * A filled box with no border, which deepens on focus.
         *
         * The plainest filled field, and the usual base for an application's own filled style:
         * nothing about it changes with status, so a `copy` that repaints the fill cannot leave a
         * stale status tint behind. That is also its limit — the container cannot show an error
         * on its own, so pair it with a [FieldGroup] message rather than relying on the box to
         * report the problem. Use [soft] when the fill itself should carry the status.
         *
         * @param shape Container shape.
         * @return Style with a neutral fill and a transparent border.
         */
        @Composable
        public fun solidNeutral(
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): FieldStyle = FieldStyle(
            bg = ArecaTheme.colors.bgSubtle,
            bgFocused = ArecaTheme.colors.bgMuted,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            bgReadOnly = ArecaTheme.colors.bgDisabled,
            border = Color.Transparent,
            content = ArecaTheme.colors.fgBase,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            extra = ArecaTheme.colors.fgMuted,
            spinner = ArecaTheme.colors.accentBase,
            placeholder = ArecaTheme.colors.fgMuted,
            shape = shape,
            label = defaultLabel(),
            message = defaultMessage(),
            iconButton = IconButtonStyle.plain(ActionColor.Neutral),
        )
    }
}

/**
 * Opacity the stock styles draw their focus ring at.
 *
 * A ring is a halo rather than a second border: at full strength it competes with the border it
 * surrounds, and the two together read as a field that has doubled in weight.
 */
private const val FocusRingAlpha = 0.24f

/**
 * The one state a field is drawn in, chosen from everything that might apply.
 *
 * States do not blend. A field that is focused *and* in error is drawn as an error, because the
 * problem is the more useful of the two things to say; a disabled field says nothing else at all.
 * Every role — background, border, value, placeholder, label, message — maps from this, so the
 * order is defined once and none of them can drift out of agreement with the others.
 */
public enum class FieldVisualState {
    /** The field rejects input entirely. Outranks everything. */
    Disabled,

    /** The value can be read and selected but not changed. Outranks any status. */
    ReadOnly,

    /** [FieldStatus.Error] is being reported. */
    Error,

    /** [FieldStatus.Warning] is being reported. */
    Warning,

    /** [FieldStatus.Success] is being reported. */
    Success,

    /** The field holds focus and has nothing to report. */
    Focused,

    /** The pointer is over the field, which does not hold focus. */
    Hovered,

    /** Nothing applies. */
    Rest,
}

/**
 * Resolves everything that might apply to a field into the one state it is drawn in.
 *
 * @param status Validation outcome being reported.
 * @param enabled Whether the field accepts input.
 * @param readOnly Whether the value can be changed.
 * @param focused Whether the field holds focus.
 * @param hovered Whether the pointer is over the field. Only reaches the drawing when nothing
 *   louder applies, and never on a touch screen, where there is no pointer to hover with.
 * @return The single state that applies.
 */
internal fun fieldVisualState(
    status: FieldStatus,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    focused: Boolean = false,
    hovered: Boolean = false,
): FieldVisualState = when {
    !enabled -> FieldVisualState.Disabled
    readOnly -> FieldVisualState.ReadOnly
    status == FieldStatus.Error -> FieldVisualState.Error
    status == FieldStatus.Warning -> FieldVisualState.Warning
    status == FieldStatus.Success -> FieldVisualState.Success
    focused -> FieldVisualState.Focused
    hovered -> FieldVisualState.Hovered
    else -> FieldVisualState.Rest
}

/**
 * Picks the container background for the state the field is in.
 *
 * @param style Palette to read the colors from.
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun resolveBackgroundColor(
    style: FieldStyle,
    state: FieldVisualState,
) = when (state) {
    FieldVisualState.Disabled -> style.bgDisabled
    FieldVisualState.ReadOnly -> style.bgReadOnly
    FieldVisualState.Error -> style.bgError
    FieldVisualState.Warning -> style.bgWarning
    FieldVisualState.Success -> style.bgSuccess
    FieldVisualState.Focused -> style.bgFocused
    FieldVisualState.Hovered -> style.bgHovered
    FieldVisualState.Rest -> style.bg
}

/**
 * Picks the container border color for the state the field is in.
 *
 * @param style Palette to read the colors from.
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun resolveBorderColor(
    style: FieldStyle,
    state: FieldVisualState,
) = when (state) {
    FieldVisualState.Disabled -> style.borderDisabled
    FieldVisualState.ReadOnly -> style.borderReadOnly
    FieldVisualState.Error -> style.borderError
    FieldVisualState.Warning -> style.borderWarning
    FieldVisualState.Success -> style.borderSuccess
    FieldVisualState.Focused -> style.borderFocused
    FieldVisualState.Hovered -> style.borderHovered
    FieldVisualState.Rest -> style.border
}

/**
 * Picks the value's foreground color for the state the field is in.
 *
 * @param style Palette to read the colors from.
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun resolveContentColor(
    style: FieldStyle,
    state: FieldVisualState,
) = when (state) {
    FieldVisualState.Disabled -> style.contentDisabled
    FieldVisualState.ReadOnly -> style.contentReadOnly
    FieldVisualState.Error -> style.contentError
    FieldVisualState.Warning -> style.contentWarning
    FieldVisualState.Success -> style.contentSuccess
    FieldVisualState.Focused -> style.contentFocused
    FieldVisualState.Hovered -> style.contentHovered
    FieldVisualState.Rest -> style.content
}

/**
 * Picks the placeholder foreground for the state the field is in.
 *
 * @param style Palette to read the colors from.
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun resolvePlaceholderColor(
    style: FieldStyle,
    state: FieldVisualState,
) = when (state) {
    FieldVisualState.Disabled -> style.placeholderDisabled
    FieldVisualState.ReadOnly -> style.placeholderReadOnly
    FieldVisualState.Error -> style.placeholderError
    FieldVisualState.Warning -> style.placeholderWarning
    FieldVisualState.Success -> style.placeholderSuccess
    FieldVisualState.Focused -> style.placeholderFocused
    FieldVisualState.Hovered -> style.placeholderHovered
    FieldVisualState.Rest -> style.placeholder
}

/**
 * Picks the focus ring color for the state the field is in.
 *
 * The ring answers focus, so it is transparent whenever the field does not hold any — including
 * under a status, which colors the ring but cannot summon one. Disabled and read-only fields never
 * ring at all: neither takes input, so a halo would promise something that is not on offer.
 *
 * @param style Palette to read the colors from.
 * @param state State the field is drawn in.
 * @param focused Whether the field, or anything inside it, holds focus.
 * @return The ring color, or [Color.Transparent] when no ring applies.
 */
internal fun resolveFocusRingColor(
    style: FieldStyle,
    state: FieldVisualState,
    focused: Boolean,
): Color = when {
    !focused -> Color.Transparent
    state == FieldVisualState.Error -> style.focusRingError
    state == FieldVisualState.Warning -> style.focusRingWarning
    state == FieldVisualState.Success -> style.focusRingSuccess
    state == FieldVisualState.Disabled || state == FieldVisualState.ReadOnly -> Color.Transparent
    else -> style.focusRing
}

/**
 * Picks the label foreground for the state the field is in.
 *
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun FieldLabelStyle.colorFor(state: FieldVisualState): Color = when (state) {
    FieldVisualState.Disabled -> contentDisabled
    FieldVisualState.ReadOnly -> contentReadOnly
    FieldVisualState.Error -> contentError
    FieldVisualState.Warning -> contentWarning
    FieldVisualState.Success -> contentSuccess
    FieldVisualState.Focused -> contentFocused
    FieldVisualState.Hovered, FieldVisualState.Rest -> content
}

/**
 * Picks the message foreground for the state the field is in.
 *
 * A message has nothing to say about focus, hover or read-only, so all three read as the resting
 * color: the message reports the value, not the interaction.
 *
 * @param state State the field is drawn in.
 * @return The single color that applies.
 */
internal fun FieldMessageStyle.colorFor(state: FieldVisualState): Color = when (state) {
    FieldVisualState.Disabled -> contentDisabled
    FieldVisualState.Error -> contentError
    FieldVisualState.Warning -> contentWarning
    FieldVisualState.Success -> contentSuccess
    FieldVisualState.ReadOnly,
    FieldVisualState.Focused,
    FieldVisualState.Hovered,
    FieldVisualState.Rest,
    -> content
}
